
undefined4 get_md_bin_capability(void)

{
  uint uVar1;
  undefined4 uVar2;
  
  uVar1 = get_md_img_type();
  if (uVar1 < 0xf) {
    uVar2 = *(undefined4 *)(md_img_capability_map + (ulong)uVar1 * 4);
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}

