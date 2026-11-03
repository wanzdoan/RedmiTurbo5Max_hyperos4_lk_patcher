
void FUN_ffff0000b801868c(undefined8 param_1,long param_2,undefined8 param_3)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff0000b804a99c(s_gsort_ffff0000b8099a60);
  if (lVar1 == 0) {
    FUN_ffff0000b804c33c(s_Parttion__gsort__is_not_exist__ffff0000b80924be);
    return;
  }
  lVar2 = FUN_ffff0000b804aab8(lVar1,param_1,param_3,param_2);
  if (lVar2 == param_2) {
    FUN_ffff0000b804c33c(s_gsort_read_size___zu_ffff0000b8097e3b,param_2);
    return;
  }
  FUN_ffff0000b804c33c(s_Wirte_gsort_size__ld_is_not_equa_ffff0000b809c449,lVar2,param_2);
  FUN_ffff0000b804a920(lVar1);
  return;
}


