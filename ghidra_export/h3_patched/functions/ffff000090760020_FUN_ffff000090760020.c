// ffff000090760020  size=320  FUN_ffff000090760020

undefined8 FUN_ffff000090760020(void)

{
  long lVar1;
  char *pcVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar1 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
  if (lVar1 == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___open_partition__s_f_ffff0000907de793,
               s_get_partition_total_size_ffff0000907d4d53,0x65,s_BLACKBOX_ffff0000907d9988,
               s_blackbox_ffff0000907f0c04);
  }
  else {
    uVar4 = *(ulong *)(lVar1 + 0x20);
    FUN_ffff00009077de54
              (s__s__d____s___The_size_of_partiti_ffff0000907dce28,
               s_get_partition_total_size_ffff0000907d4d53,0x69,s_BLACKBOX_ffff0000907d9988,
               s_blackbox_ffff0000907f0c04,uVar4);
    FUN_ffff00009077bce4(lVar1);
    if (uVar4 - 0x9a00000 < 0xa00001) {
      pcVar2 = s__s__d__blackbox_v1_ffff0000907e50bb;
      uVar3 = 0x75;
      goto LAB_ffff0000907600e4;
    }
    if (uVar4 - 0xb400000 >> 0x16 < 5) {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v2_ffff0000907d9991,s_get_black_box_version_ffff0000907f4fb9,0x78
                );
      return 2;
    }
    if (0x18 < uVar4 >> 0x17) {
      FUN_ffff00009077de54
                (s__s__d__blackbox_v3_ffff0000907d813b,s_get_black_box_version_ffff0000907f4fb9,0x7b
                );
      return 3;
    }
  }
  pcVar2 = s__s__d__the_size_of_blackbox_part_ffff0000907d99a5;
  uVar3 = 0x7e;
LAB_ffff0000907600e4:
  FUN_ffff00009077de54(pcVar2,s_get_black_box_version_ffff0000907f4fb9,uVar3);
  return 1;
}


