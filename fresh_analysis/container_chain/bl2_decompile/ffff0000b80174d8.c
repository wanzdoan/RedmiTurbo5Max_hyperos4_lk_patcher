
void FUN_ffff0000b80174d8(void)

{
  long *plVar1;
  long local_30;
  long lStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  plVar1 = (long *)FUN_ffff0000b800db20(&DAT_ffff0000b808b6c5,0);
  if (plVar1 != (long *)0x0) {
    uStack_18 = 0;
    local_30 = *plVar1;
    local_20 = 3;
    lStack_28 = plVar1[1] + local_30;
    FUN_ffff0000b805305c(&local_30);
    return;
  }
  FUN_ffff0000b804c33c(s__GZ_INIT__query_gz_s_reserved_me_ffff0000b80916fa);
  return;
}


