// File offset entry: 0xa6084

undefined8 FUN_020a5f84(undefined4 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = func_0x0209c6dc();
  if (iVar1 == 0) {
    uVar2 = 0x10101a;
  }
  else {
    FUN_02091664(0x2123858,0,0x200);
    func_0x020a0dfc(0x2123858,0x21240ec,param_1);
    uRam0000000002123a58 = param_1;
    func_0x020a0fac(0x2123858,param_1);
    uVar2 = 0;
  }
  return uVar2;
}


