
undefined8 FUN_ffff0000907602b8(void)

{
  long lVar1;
  char *pcVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar1 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
  if (lVar1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___open_partition__s_f_ffff0000907de84d,
               s_get_partition_total_size_ffff0000907d4d90,0x65,s_BLACKBOX_ffff0000907d9a1f,
               s_blackbox_ffff0000907f0d8c);
  }
  else {
    uVar4 = *(ulong *)(lVar1 + 0x20);
    FUN_ffff00009077e11c
              (s__s__d____s___The_size_of_partiti_ffff0000907dcf15,
               s_get_partition_total_size_ffff0000907d4d90,0x69,s_BLACKBOX_ffff0000907d9a1f,
               s_blackbox_ffff0000907f0d8c,uVar4);
    FUN_ffff00009077bfac(lVar1);
    if (uVar4 - 0x9a00000 < 0xa00001) {
      pcVar2 = s__s__d__blackbox_v1_ffff0000907e51d6;
      uVar3 = 0x75;
      goto LAB_ffff00009076037c;
    }
    if (uVar4 - 0xb400000 >> 0x16 < 5) {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v2_ffff0000907d9a28,s_get_black_box_version_ffff0000907f513d,0x78
                );
      return 2;
    }
    if (0x18 < uVar4 >> 0x17) {
      FUN_ffff00009077e11c
                (s__s__d__blackbox_v3_ffff0000907d81d1,s_get_black_box_version_ffff0000907f513d,0x7b
                );
      return 3;
    }
  }
  pcVar2 = s__s__d__the_size_of_blackbox_part_ffff0000907d9a3c;
  uVar3 = 0x7e;
LAB_ffff00009076037c:
  FUN_ffff00009077e11c(pcVar2,s_get_black_box_version_ffff0000907f513d,uVar3);
  return 1;
}


