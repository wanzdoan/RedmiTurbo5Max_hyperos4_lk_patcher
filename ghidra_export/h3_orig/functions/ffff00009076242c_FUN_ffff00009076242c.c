// ffff00009076242c  size=648  FUN_ffff00009076242c

void FUN_ffff00009076242c(void)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  
  if (DAT_ffff00009087bf14 == 1) {
    pcVar2 = s__s__d____s___blackbox_v1__no_zon_ffff0000907f8a30;
    uVar3 = 0x463;
    goto LAB_ffff000090762468;
  }
  if ((DAT_ffff00009087bf14 & 0xfe) == 2) {
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf18,0xa400000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_control_zone__ffff0000907d9a8e;
      uVar3 = 0x46a;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf30,0xa420000,0x14);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_record_zone_h_ffff00009080129a;
      uVar3 = 0x470;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf48,0xa440000,0x28);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_journal_zone__ffff0000907e68a1;
      uVar3 = 0x476;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087bf70,0xa440400,0x2f4);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_reb_ffff000090802b34;
      uVar3 = 0x47c;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087c268,0xac10800,0x18c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_exception_shu_ffff0000907e68d1;
      uVar3 = 0x482;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087c3f8,0xac60c00,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_fastboot_mode_ffff0000907fbf50;
      uVar3 = 0x488;
      goto LAB_ffff000090762468;
    }
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087c628,0xb00a800,0x22c);
    if ((uVar1 & 1) == 0) {
      pcVar2 = s__s__d____s___write_recovery_mode_ffff0000907e819a;
      uVar3 = 0x48e;
      goto LAB_ffff000090762468;
    }
  }
  if ((DAT_ffff00009087bf14 != 3) ||
     (uVar1 = FUN_ffff000090760d38
                        (s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087c858,0xb3b4400,0x2f4),
     (uVar1 & 1) != 0)) {
    return;
  }
  pcVar2 = s__s__d____s___write_normal_boot_l_ffff0000907d4f03;
  uVar3 = 0x496;
LAB_ffff000090762468:
  FUN_ffff00009077de54
            (pcVar2,s_WriteBlackboxZoneHeader_ffff000090802b1c,uVar3,s_BLACKBOX_ffff0000907d9988);
  return;
}


