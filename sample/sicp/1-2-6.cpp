#include <iostream>

#define LL long long

using std::cin;
using std::cout;
using std::endl;

LL exp_mod(LL a, LL b, LL m) {
	LL ret = 1;
	while (b) {
		if (b & 1)
			ret = (ret * a) % m;
		b >>= 1;
		a = (a * a) % m;
	}
	return ret;
}

auto main(const int argc, const char *argv[]) -> int {
	LL a, b, m;
	cin >> a >> b >> m;
	cout << exp_mod(a, b, m) << endl;
	return 0;
}