// File offset entry: 0xa780

undefined4 FUN_0200a680(long param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  uVar2 = 0x10a00a;
  if (param_1 != 0) {
    iVar1 = func_0x0200341c(uRam0000000002103328,param_1,0x10);
    uVar2 = 0;
    if (iVar1 != 0) {
      uVar2 = 0x10a010;
    }
  }
  return uVar2;
}


