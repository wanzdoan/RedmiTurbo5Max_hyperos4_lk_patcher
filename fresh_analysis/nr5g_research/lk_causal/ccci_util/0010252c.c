
uint get_md_img_type(void)

{
  return -((byte)s_g_lk_load_img_status & 1) & md_type_at_lk;
}

