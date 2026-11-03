
void FUN_ffff0000be013510(undefined8 param_1,long param_2)

{
  long lVar1;
  long lVar2;
  undefined8 *puVar3;
  
  if (param_2 != 0) {
    lVar1 = FUN_ffff0000be060d00();
    lVar2 = FUN_ffff0000be060d00(param_2);
    puVar3 = (undefined8 *)FUN_ffff0000be05f1bc(0x18);
    if (((puVar3 == (undefined8 *)0x0) || (lVar1 == 0)) || (lVar2 == 0)) {
      if (puVar3 != (undefined8 *)0x0) {
        thunk_FUN_ffff0000be09937c();
      }
      if (lVar1 != 0) {
        thunk_FUN_ffff0000be09937c(lVar1);
      }
      if (lVar2 != 0) {
        thunk_FUN_ffff0000be09937c(lVar2);
        return;
      }
    }
    else {
      puVar3[1] = lVar1;
      puVar3[2] = lVar2;
      *puVar3 = DAT_ffff0000be107958;
      DAT_ffff0000be107958 = puVar3;
    }
  }
  return;
}


