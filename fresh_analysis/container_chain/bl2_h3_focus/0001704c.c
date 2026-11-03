// File offset entry: 0x1704c

void FUN_ffff0000b801704c(void)

{
  long *plVar1;
  long local_30;
  long lStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  plVar1 = (long *)FUN_ffff0000b800db20(&DAT_ffff0000b808a665,0);
  if (plVar1 != (long *)0x0) {
    uStack_18 = 0;
    local_30 = *plVar1;
    local_20 = 3;
    lStack_28 = plVar1[1] + local_30;
    FUN_ffff0000b8052bd0(&local_30);
    return;
  }
  FUN_ffff0000b804beb0(s__GZ_INIT__query_gz_s_reserved_me_ffff0000b8090671);
  return;
}


