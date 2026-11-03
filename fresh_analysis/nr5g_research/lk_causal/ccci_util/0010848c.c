
undefined8 set_soc_md_rt_rat_by_idx(uint param_1)

{
  uint uVar1;
  undefined *puVar2;
  
  uVar1 = get_md_img_type();
  if (uVar1 < 0xf) {
    uVar1 = *(uint *)(md_img_capability_map + (ulong)uVar1 * 4);
  }
  else {
    uVar1 = 0;
  }
  if (param_1 != 0) {
    if (param_1 < 0x1b) {
      if (7 < param_1) {
        if ((*(uint *)(md_rat_map + (ulong)param_1 * 4) & (uVar1 ^ 0xffffffff)) != 0) {
          _printk(&DAT_00111004,"set_soc_md_rt_rat_by_idx",param_1);
          return 0xffffffff;
        }
        s_md_rt_wm_id_bitmap = *(uint *)(md_rat_map + (ulong)param_1 * 4);
        s_md_rt_wm_id = param_1;
        _printk(&DAT_001120dd,"set_soc_md_rt_rat_by_idx",param_1);
        return 0;
      }
      puVar2 = &DAT_001125ff;
    }
    else {
      puVar2 = &DAT_001111bc;
    }
    _printk(puVar2,"set_soc_md_rt_rat_by_idx",param_1);
  }
  return 0xffffffff;
}

