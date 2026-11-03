
undefined8 FUN_ffff0000be069ae8(void)

{
  uint *puVar1;
  char *pcVar2;
  bool bVar3;
  uint uVar4;
  
  FUN_ffff0000be0194d8(0x277);
  bVar3 = (2 < DAT_ffff0000be134754 & DAT_ffff0000be1348a5 >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar3) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar4 = 0xfffffffe;
  if (bVar3) {
    uVar4 = 0xffffffbe;
  }
  pcVar2 = s__USB__U3_pullup_D_s_ffff0000be0c12a9;
  if (bVar3) {
    pcVar2 = s__USB__U2_pullup_D_s_ffff0000be0abe3d;
  }
  *puVar1 = uVar4 & *puVar1;
  FUN_ffff0000be05fd98(pcVar2,&DAT_ffff0000be0adefe);
  FUN_ffff0000be06c470();
  DAT_ffff0000be134734 = 0;
  return 0;
}


