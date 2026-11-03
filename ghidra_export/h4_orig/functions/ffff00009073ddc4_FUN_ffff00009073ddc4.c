// ffff00009073ddc4  size=152  FUN_ffff00009073ddc4

undefined8 FUN_ffff00009073ddc4(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff00009077d514(2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff00009077f6fc
                      (lVar2 + *param_2,2000 - (long)*param_2,
                       s_____________________________last_ffff0000907efc3b);
    *param_2 = *param_2 + iVar1;
    FUN_ffff00009073dbf8(&DAT_ffff000090819f68,lVar2,param_2);
    uVar3 = 1;
    if (2000 < *param_2) {
      *param_2 = 2000;
    }
  }
  return uVar3;
}


