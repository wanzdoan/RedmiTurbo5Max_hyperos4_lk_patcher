// ffff000090729024  size=80  FUN_ffff000090729024

undefined8 FUN_ffff000090729024(long param_1)

{
  char *pcVar1;
  uint in_w5;
  
  pcVar1 = &DAT_ffff0000907d298b;
  if (in_w5 < 0x191) {
    pcVar1 = s_ST_FT_ffff0000907e5ef8;
  }
  FUN_ffff00009077de54
            (s__s__d__i2c_d__s_mode__unsupporte_ffff0000907f4487,
             s_mtk_i2c_calculate_speed_ffff0000907d2973,0x6b,*(undefined2 *)(param_1 + 8),pcVar1);
  return 0xfffffff9;
}


