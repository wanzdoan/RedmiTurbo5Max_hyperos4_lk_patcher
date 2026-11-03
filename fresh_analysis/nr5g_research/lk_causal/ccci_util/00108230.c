
bool check_rat_at_md_img(void)

{
  uint uVar1;
  uint uVar2;
  
  uVar1 = get_capability_bit();
  uVar2 = get_md_img_type();
  if (uVar2 < 0xf) {
    uVar2 = *(uint *)(md_img_capability_map + (ulong)uVar2 * 4);
  }
  else {
    uVar2 = 0;
  }
  return (uVar2 & uVar1) != 0;
}

