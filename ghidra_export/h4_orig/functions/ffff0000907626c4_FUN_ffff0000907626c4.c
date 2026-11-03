// ffff0000907626c4  size=648  FUN_ffff0000907626c4

void FUN_ffff0000907626c4(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  
  if (DAT_ffff00009087bfac == 1) {
    pcVar2 = s__s__d____s___blackbox_v1__no_zon_ffff0000907f8bcc;
    uVar3 = 0x463;
    goto LAB_ffff000090762700;
  }
  if ((DAT_ffff00009087bfac & 0xfe) == 2) {
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfb0,0xa400000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_control_zone__ffff0000907d9b25;
      uVar3 = 0x46a;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfc8,0xa420000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_record_zone_h_ffff000090801476;
      uVar3 = 0x470;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087bfe0,0xa440000,0x28);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_journal_zone__ffff0000907e69b5;
      uVar3 = 0x476;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c008,0xa440400,0x2f4);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_reb_ffff000090802d38;
      uVar3 = 0x47c;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c300,0xac10800,0x18c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_shu_ffff0000907e69e5;
      uVar3 = 0x482;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c490,0xac60c00,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_fastboot_mode_ffff0000907fc0ec;
      uVar3 = 0x488;
      goto LAB_ffff000090762700;
    }
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c6c0,0xb00a800,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_recovery_mode_ffff0000907e82de;
      uVar3 = 0x48e;
      goto LAB_ffff000090762700;
    }
  }
  if ((DAT_ffff00009087bfac != 3) ||
     (uVar1 = FUN_ffff000090760fd0
                        (s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087c8f0,0xb3b4400,0x2f4),
     (uVar1 & 1) != 0)) {
    return;
  }
  pcVar2 = s__s__d____s___write_normal_boot_l_ffff0000907d4f40;
  uVar3 = 0x496;
LAB_ffff000090762700:
  FUN_ffff00009077e11c
            (pcVar2,s_WriteBlackboxZoneHeader_ffff000090802d20,uVar3,s_BLACKBOX_ffff0000907d9a1f);
  return;
}


