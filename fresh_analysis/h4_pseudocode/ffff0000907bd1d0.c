
long FUN_ffff0000907bd1d0(undefined8 param_1,undefined8 param_2,long *param_3)

{
  undefined8 local_40;
  long local_38;
  long local_18;
  
  local_18 = 0;
  FUN_ffff0000907bce70(param_1,param_2,&LAB_ffff0000907bd264,&local_18);
  local_40 = 0;
  local_38 = FUN_ffff0000907c15dc(local_18 * 8 + 8);
  if ((local_38 != 0) &&
     (FUN_ffff0000907bce70(param_1,param_2,&LAB_ffff0000907bd280,&local_40), param_3 != (long *)0x0)
     ) {
    *param_3 = local_18;
  }
  return local_38;
}


