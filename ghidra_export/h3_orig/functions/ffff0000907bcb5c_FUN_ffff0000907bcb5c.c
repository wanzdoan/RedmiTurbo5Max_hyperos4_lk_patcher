// ffff0000907bcb5c  size=148  FUN_ffff0000907bcb5c

long FUN_ffff0000907bcb5c(undefined8 param_1,undefined8 param_2,long *param_3)

{
  undefined8 local_40;
  long local_38;
  long local_18;
  
  local_18 = 0;
  FUN_ffff0000907bc7fc(param_1,param_2,FUN_ffff0000907bcbf0,&local_18);
  local_40 = 0;
  local_38 = FUN_ffff0000907c0f68(local_18 * 8 + 8);
  if ((local_38 != 0) &&
     (FUN_ffff0000907bc7fc(param_1,param_2,FUN_ffff0000907bcc0c,&local_40), param_3 != (long *)0x0))
  {
    *param_3 = local_18;
  }
  return local_38;
}


