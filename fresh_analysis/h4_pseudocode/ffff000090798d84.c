
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090798d84(undefined8 *param_1)

{
  _DAT_ffff000088410040 = FUN_ffff00009077b334(*param_1);
  _DAT_ffff000088410048 = *(undefined4 *)(param_1 + 2);
  _DAT_ffff000088410050 = FUN_ffff00009077b334((long)param_1 + 0x15);
  _DAT_ffff000088410058 = (uint)*(byte *)((long)param_1 + 0x14);
  _DAT_ffff000088410060 = FUN_ffff00009077b334((long)param_1 + 0x36);
  _DAT_ffff000088410068 = (uint)*(byte *)((long)param_1 + 0x35);
  if (*(char *)((long)param_1 + 0x76) == '\0') {
    _DAT_ffff000088410088 = 0;
  }
  else {
    FUN_ffff00009077e11c
              (s__SEC___s__dec_block_>iv2_sz_0x_x_ffff0000908019ce,s_aes_smem_init_ffff0000907fabfa)
    ;
    _DAT_ffff000088410070 = FUN_ffff00009077b334((long)param_1 + 0x56);
    _DAT_ffff000088410080 = FUN_ffff00009077b334((long)param_1 + 0x66);
    _DAT_ffff000088410088 = (uint)*(byte *)((long)param_1 + 0x76);
  }
  FUN_ffff000090707c70(0xffff000088410000,0x10000);
  return 0;
}


