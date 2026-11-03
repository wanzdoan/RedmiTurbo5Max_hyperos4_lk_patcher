// ffff000090739d2c  size=100  FUN_ffff000090739d2c

undefined8 FUN_ffff000090739d2c(long param_1,int param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar1 = 0x6afbffff;
  if (DAT_ffff00009081942c != 1) {
    uVar1 = 0x6aefffff;
  }
  uVar3 = 0x40000;
  if (DAT_ffff00009081942c != 1) {
    uVar3 = 0x100000;
  }
  uVar2 = 0;
  if (param_2 != 0) {
    uVar2 = uVar3;
  }
  FUN_ffff000090739b00
            ((uint *)(param_1 + 0x44),*(uint *)(param_1 + 0x44) & uVar1 | uVar2 | 0x95000000);
  return 0;
}


