
void set_soc_md_rt_rat_str(void)

{
  long lVar1;
  uint uVar2;
  long lVar3;
  
  lVar3 = sp_el0;
  lVar3 = *(long *)(lVar3 + 0x620);
  uVar2 = get_md_img_type();
  if ((uVar2 < 0xf) && (*(int *)(md_img_capability_map + (ulong)uVar2 * 4) - 0x13U < 0x6d)) {
                    /* WARNING: Could not emulate address calculation at 0x001085d8 */
                    /* WARNING: Treating indirect jump as call */
    (*(code *)(&UNK_001085e4 +
              (ulong)(byte)(&DAT_00113ad0)
                           [*(int *)(md_img_capability_map + (ulong)uVar2 * 4) - 0x13U] * 4))();
    return;
  }
  _printk(&DAT_00111439,"set_soc_md_rt_rat_str");
  lVar1 = sp_el0;
  if (*(long *)(lVar1 + 0x620) == lVar3) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(0xffffffff);
}

