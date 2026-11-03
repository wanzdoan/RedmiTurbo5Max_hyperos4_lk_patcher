
undefined8 FUN_ffff00009078bf5c(void)

{
  uint *puVar1;
  char *pcVar2;
  bool bVar3;
  uint uVar4;
  
  FUN_ffff0000907164e0(0x277);
  bVar3 = (2 < DAT_ffff00009088a0cc & DAT_ffff00009088a21d >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar3) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar4 = 0xfffffffe;
  if (bVar3) {
    uVar4 = 0xffffffbe;
  }
  pcVar2 = s__USB__U3_pullup_D_s_ffff0000907fa9ac;
  if (bVar3) {
    pcVar2 = s__USB__U2_pullup_D_s_ffff0000907db9eb;
  }
  *puVar1 = uVar4 & *puVar1;
  FUN_ffff00009077e11c(pcVar2,&DAT_ffff0000907dd9b4);
  FUN_ffff00009078e8e4();
  DAT_ffff00009088a0b0 = 0;
  return 0;
}


