
void FUN_ffff0000be04e7a4(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  
  if (DAT_ffff0000be1261e0 == 1) {
    pcVar2 = s__s__d____s___blackbox_v1__no_zon_ffff0000be0bfe4d;
    uVar3 = 0x463;
    goto LAB_ffff0000be04e7e0;
  }
  if ((DAT_ffff0000be1261e0 & 0xfe) == 2) {
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1261e8,0xa400000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_control_zone__ffff0000be0aaaf4;
      uVar3 = 0x46a;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126200,0xa420000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_record_zone_h_ffff0000be0c57ff;
      uVar3 = 0x470;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126218,0xa440000,0x28);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_journal_zone__ffff0000be0b3426;
      uVar3 = 0x476;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126240,0xa440400,0x2f4);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_reb_ffff0000be0c6756;
      uVar3 = 0x47c;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126538,0xac10800,0x18c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_shu_ffff0000be0b3456;
      uVar3 = 0x482;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1266c8,0xac60c00,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_fastboot_mode_ffff0000be0c218e;
      uVar3 = 0x488;
      goto LAB_ffff0000be04e7e0;
    }
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&DAT_ffff0000be1268f8,0xb00a800,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_recovery_mode_ffff0000be0b4823;
      uVar3 = 0x48e;
      goto LAB_ffff0000be04e7e0;
    }
  }
  if ((DAT_ffff0000be1261e0 != 3) ||
     (uVar1 = FUN_ffff0000be04d0b0
                        (s_blackbox_ffff0000be0bac45,&DAT_ffff0000be126b28,0xb3b4400,0x2f4),
     (uVar1 & 1) != 0)) {
    return;
  }
  pcVar2 = s__s__d____s___write_normal_boot_l_ffff0000be0a75a5;
  uVar3 = 0x496;
LAB_ffff0000be04e7e0:
  FUN_ffff0000be05fd98
            (pcVar2,s_WriteBlackboxZoneHeader_ffff0000be0c673e,uVar3,s_BLACKBOX_ffff0000be0aa9ee);
  return;
}


