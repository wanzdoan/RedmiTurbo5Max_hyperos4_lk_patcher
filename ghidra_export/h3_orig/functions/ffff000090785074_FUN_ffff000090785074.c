// ffff000090785074  size=328  FUN_ffff000090785074

undefined8
FUN_ffff000090785074(uint *param_1,uint param_2,undefined4 *param_3,uint *param_4,uint *param_5)

{
  uint uVar1;
  uint uVar2;
  undefined8 uVar3;
  ulong uVar4;
  ulong uVar5;
  uint *puVar6;
  
  if (param_1 == (uint *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dtb_ops_ffff0000907eec5b,0x44,
               s_dt_tbl_hdr_buffer_ffff0000907d8575);
  }
  if (param_3 == (undefined4 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dtb_ops_ffff0000907eec5b,0x45,
               s_dtbo_entry_idx_out_ffff0000907d6bf6);
  }
  if (param_4 == (uint *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dtb_ops_ffff0000907eec5b,0x46,s_dtbo_size_ffff0000907f3655
              );
  }
  if (param_5 == (uint *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dtb_ops_ffff0000907eec5b,0x47,
               s_dtbo_offset_ffff0000907fc3f9);
  }
  uVar1 = *param_1;
  if (uVar1 == 0x1eabb7d7) {
    uVar2 = param_1[4];
    uVar4 = (ulong)uVar2;
    uVar1 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
    uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
    if (uVar2 != 0) {
      uVar5 = 0;
      if (uVar1 < 2) {
        uVar1 = 1;
      }
      uVar4 = (ulong)uVar1;
      puVar6 = param_1 + 10;
      do {
        uVar1 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
        if ((uVar1 >> 0x10 | uVar1 << 0x10) == param_2) goto LAB_ffff000090785108;
        uVar5 = uVar5 + 1;
        puVar6 = puVar6 + 8;
      } while (uVar4 != uVar5);
    }
    FUN_ffff00009077de54(s_Set_dtbo_entry_idx_to_0__dtbo_en_ffff0000907eec83,uVar4);
    uVar5 = 0;
LAB_ffff000090785108:
    *param_3 = (int)uVar5;
    uVar3 = 0;
    uVar1 = param_1[(uVar5 & 0xffffffff) * 8 + 8];
    uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
    *param_4 = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar1 = (param_1 + (uVar5 & 0xffffffff) * 8 + 8)[1];
    uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
    *param_5 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  else {
    uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
    FUN_ffff00009077de54
              (s_dt_table_header_magic_is_not_cor_ffff0000907d3820,uVar1 >> 0x10 | uVar1 << 0x10);
    uVar3 = 0xfffffff9;
  }
  return uVar3;
}


