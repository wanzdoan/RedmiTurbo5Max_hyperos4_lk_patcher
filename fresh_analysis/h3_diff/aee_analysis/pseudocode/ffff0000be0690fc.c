
void FUN_ffff0000be0690fc(void)

{
  bool bVar1;
  uint *puVar2;
  char *pcVar3;
  undefined *puVar4;
  bool bVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  uint local_4;
  
  local_4 = 0;
  iVar6 = FUN_ffff0000be004258(&local_4);
  if (iVar6 == 0) {
    bVar5 = 0x3d0ce7 < (ulong)local_4;
    uVar7 = (ulong)local_4 / 1000;
  }
  else {
    FUN_ffff0000be05fd98(s__USB__fail_to_get_vbus_ffff0000be0c59fa);
    uVar7 = 0;
    bVar5 = false;
  }
  if ((DAT_ffff0000be134734 == '\0') || (bVar5)) {
    bVar1 = false;
    if (DAT_ffff0000be134734 == '\0') {
      bVar1 = bVar5;
    }
    if (bVar1) {
      DAT_ffff0000be134738 = 0;
      FUN_ffff0000be05fd98(s__USB__detect_plug_in__vbus__d_ffff0000be0a9a08,uVar7);
      FUN_ffff0000be0695f4();
    }
    else {
      DAT_ffff0000be134738 = DAT_ffff0000be134738 + 1;
      if (DAT_ffff0000be134738 == 0x78) {
        puVar4 = &DAT_ffff0000be0aef80;
        if (DAT_ffff0000be134734 != '\0') {
          puVar4 = &DAT_ffff0000be0c3f65;
        }
        DAT_ffff0000be134738 = 0;
        FUN_ffff0000be05fd98(s__USB__remain_power__s__vbus__d_ffff0000be0c69cf,puVar4);
      }
    }
  }
  else {
    DAT_ffff0000be134738 = 0;
    FUN_ffff0000be05fd98(s__USB__detect_plug_out__vbus__d_ffff0000be0b8006,uVar7);
    FUN_ffff0000be0194d8(0x277);
    bVar5 = (2 < DAT_ffff0000be134754 & DAT_ffff0000be1348a5 >> 1) == 0;
    puVar2 = (uint *)&DAT_ffff00001670241c;
    if (bVar5) {
      puVar2 = (uint *)&DAT_ffff000016703404;
    }
    uVar8 = 0xfffffffe;
    if (bVar5) {
      uVar8 = 0xffffffbe;
    }
    pcVar3 = s__USB__U3_pullup_D_s_ffff0000be0c12a9;
    if (bVar5) {
      pcVar3 = s__USB__U2_pullup_D_s_ffff0000be0abe3d;
    }
    *puVar2 = uVar8 & *puVar2;
    FUN_ffff0000be05fd98(pcVar3,&DAT_ffff0000be0adefe);
    FUN_ffff0000be06c470();
    DAT_ffff0000be134734 = '\0';
  }
  return;
}


