// ffff00009073db2c  size=152  FUN_ffff00009073db2c

undefined8 FUN_ffff00009073db2c(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff00009077d24c(2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff00009077f434
                      (lVar2 + *param_2,2000 - (long)*param_2,
                       s_____________________________last_ffff0000907efaca);
    *param_2 = *param_2 + iVar1;
    FUN_ffff00009073d960(&DAT_ffff000090819f48,lVar2,param_2);
    uVar3 = 1;
    if (2000 < *param_2) {
      *param_2 = 2000;
    }
  }
  return uVar3;
}


