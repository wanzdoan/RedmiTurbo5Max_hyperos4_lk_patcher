// File offset entry: 0xa7f6c

undefined8 FUN_020a7e6c(uint *param_1)

{
  int iVar1;
  uint uVar2;
  
  uVar2 = 0xffffffff;
  do {
    uVar2 = uVar2 + 1;
    iVar1 = func_0x020a7e20(uVar2);
    if (2 < uVar2) break;
  } while (iVar1 != 0);
  *param_1 = uVar2;
  return 0;
}


