
void FUN_ffff0000b805ac28(void)

{
  long *plVar1;
  long local_30;
  long lStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  plVar1 = (long *)FUN_ffff0000b800db20(s_me_cmdq_reserved_ffff0000b809efb3,0);
  if (plVar1 != (long *)0x0) {
    uStack_18 = 0;
    local_30 = *plVar1;
    local_20 = 0x3a;
    lStack_28 = local_30 + plVar1[1] + -0xa0000;
    FUN_ffff0000b805305c(&local_30);
    return;
  }
  FUN_ffff0000b804c33c
            (s__cmdq__err___s__mblock_reserved_i_ffff0000b808b1d7,
             s_cmdq_reserved_set_protection_ffff0000b8085cbd);
  return;
}


