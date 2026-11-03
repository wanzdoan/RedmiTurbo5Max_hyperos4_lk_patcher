// ffff000090714bac  size=124  FUN_ffff000090714bac

void FUN_ffff000090714bac(undefined8 *param_1)

{
  *(undefined4 *)(param_1 + 2) = 0;
  *(undefined8 *)((long)param_1 + 0x14) = 0x100000;
  *param_1 = 0x627759541bcde0f0;
  param_1[1] = 0x100000001000;
  *(undefined8 *)((long)param_1 + 0x24) = 0;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x10000000001d;
  FUN_ffff00009077e858((long)param_1 + 0x2c,s_boot_error_for_hardware_ffff0000907d8d3c,0x19);
  FUN_ffff00009077e858((long)param_1 + 0xac,&DAT_ffff0000907fcb29,4);
  return;
}


