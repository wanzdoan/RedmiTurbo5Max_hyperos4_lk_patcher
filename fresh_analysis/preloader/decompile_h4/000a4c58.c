// File offset entry: 0xa4c58

undefined4 FUN_020a4b58(int *param_1)

{
  uint uVar1;
  undefined4 uVar2;
  
  uVar1 = func_0x0208cbd0();
  if (*param_1 == 1) {
    uVar2 = 0x101001;
    if ((param_1[3] == 0x2000000) && (uVar2 = 0x100001, (uint)(param_1[1] + param_1[9]) <= uVar1)) {
      uVar2 = 0;
    }
  }
  else {
    uVar2 = 0x101001;
    func_0x0208ada0(0x20b517f,0x20c9773);
  }
  return uVar2;
}


