// File offset entry: 0xac0a4

undefined4 FUN_020abfa4(long param_1,uint param_2)

{
  int iVar1;
  uint *puVar2;
  undefined4 uVar3;
  
  if ((param_2 & 0xffff0000) == 0) {
    puVar2 = (uint *)(param_1 + 0xc);
    *puVar2 = *puVar2 | param_2;
    iVar1 = func_0x020a92b0(puVar2,param_2 << 0x10,param_2 << 0x10,1000);
    uVar3 = 0;
    if (iVar1 != 0) {
      uVar3 = 0x727e;
    }
    return uVar3;
  }
  return 0x7246;
}


