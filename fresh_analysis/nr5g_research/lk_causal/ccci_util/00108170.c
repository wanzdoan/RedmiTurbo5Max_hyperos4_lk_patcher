
void mtk_ccci_get_md_img_cap(void)

{
  long lVar1;
  int iVar2;
  undefined4 local_1c;
  long local_18;
  
  lVar1 = sp_el0;
  local_18 = *(long *)(lVar1 + 0x620);
  local_1c = 0;
  iVar2 = mtk_ccci_find_args_val("md_capability",&local_1c,4);
  if (iVar2 != 4) {
    _printk(&DAT_001120c0,"mtk_ccci_get_md_img_cap");
  }
  lVar1 = sp_el0;
  if (*(long *)(lVar1 + 0x620) == local_18) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(local_1c);
}

