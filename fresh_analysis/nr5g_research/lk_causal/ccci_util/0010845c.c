
bool check_rat_at_rt_setting(void)

{
  uint uVar1;
  
  uVar1 = get_capability_bit();
  return (s_md_rt_wm_id_bitmap & uVar1) != 0;
}

