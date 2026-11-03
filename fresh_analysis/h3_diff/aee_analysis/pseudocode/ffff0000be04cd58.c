
undefined8 FUN_ffff0000be04cd58(void)

{
  long lVar1;
  char *pcVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar1 = FUN_ffff0000be05df6c(s_blackbox_ffff0000be0bac45);
  if (lVar1 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___open_partition__s_f_ffff0000be0adcfe,
               s_get_partition_total_size_ffff0000be0a742e,0x65,s_BLACKBOX_ffff0000be0aa9ee,
               s_blackbox_ffff0000be0bac45);
  }
  else {
    uVar4 = *(ulong *)(lVar1 + 0x20);
    FUN_ffff0000be05fd98
              (s__s__d____s___The_size_of_partiti_ffff0000be0acd14,
               s_get_partition_total_size_ffff0000be0a742e,0x69,s_BLACKBOX_ffff0000be0aa9ee,
               s_blackbox_ffff0000be0bac45,uVar4);
    FUN_ffff0000be05def0(lVar1);
    if (uVar4 - 0x9a00000 < 0xa00001) {
      pcVar2 = s__s__d__blackbox_v1_ffff0000be0b2437;
      uVar3 = 0x75;
      goto LAB_ffff0000be04ce1c;
    }
    if (uVar4 - 0xb400000 >> 0x16 < 5) {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v2_ffff0000be0aa9f7,s_get_black_box_version_ffff0000be0bd766,0x78
                );
      return 2;
    }
    if (0x18 < uVar4 >> 0x17) {
      FUN_ffff0000be05fd98
                (s__s__d__blackbox_v3_ffff0000be0a969f,s_get_black_box_version_ffff0000be0bd766,0x7b
                );
      return 3;
    }
  }
  pcVar2 = s__s__d__the_size_of_blackbox_part_ffff0000be0aaa0b;
  uVar3 = 0x7e;
LAB_ffff0000be04ce1c:
  FUN_ffff0000be05fd98(pcVar2,s_get_black_box_version_ffff0000be0bd766,uVar3);
  return 1;
}


