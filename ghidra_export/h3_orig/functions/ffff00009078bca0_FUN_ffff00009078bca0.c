// ffff00009078bca0  size=148  FUN_ffff00009078bca0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078bca0(void)

{
  uint *puVar1;
  char *pcVar2;
  bool bVar3;
  uint uVar4;
  
  FUN_ffff00009071639c(0x277);
  bVar3 = (2 < _DAT_ffff00009088a034 & DAT_ffff00009088a185 >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar3) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar4 = 0xfffffffe;
  if (bVar3) {
    uVar4 = 0xffffffbe;
  }
  pcVar2 = s__USB__U3_pullup_D_s_ffff0000907fa810;
  if (bVar3) {
    pcVar2 = s__USB__U2_pullup_D_s_ffff0000907db8e0;
  }
  *puVar1 = uVar4 & *puVar1;
  FUN_ffff00009077de54(pcVar2,&DAT_ffff0000907dd911);
  FUN_ffff00009078e624();
  DAT_ffff00009088a018 = 0;
  return 0;
}


