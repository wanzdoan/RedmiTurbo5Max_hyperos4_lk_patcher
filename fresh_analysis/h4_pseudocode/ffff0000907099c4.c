
void FUN_ffff0000907099c4(undefined8 param_1,long param_2)

{
  long lVar1;
  long lVar2;
  undefined8 *puVar3;
  
  if (param_2 != 0) {
    lVar1 = FUN_ffff00009077f0f4();
    lVar2 = FUN_ffff00009077f0f4(param_2);
    puVar3 = (undefined8 *)FUN_ffff00009077d514(0x18);
    if (((puVar3 == (undefined8 *)0x0) || (lVar1 == 0)) || (lVar2 == 0)) {
      if (puVar3 != (undefined8 *)0x0) {
        thunk_FUN_ffff0000907c314c();
      }
      if (lVar1 != 0) {
        thunk_FUN_ffff0000907c314c(lVar1);
      }
      if (lVar2 != 0) {
        thunk_FUN_ffff0000907c314c(lVar2);
        return;
      }
    }
    else {
      puVar3[1] = lVar1;
      puVar3[2] = lVar2;
      *puVar3 = DAT_ffff00009083d108;
      DAT_ffff00009083d108 = puVar3;
    }
  }
  return;
}


