// File offset entry: 0x91710

void FUN_02091610(ulong param_1,ulong param_2,int param_3)

{
  undefined1 *puVar1;
  undefined1 *puVar2;
  
  if (param_1 != param_2) {
    if (param_1 < param_2) {
      FUN_020915ac();
    }
    else {
      puVar1 = (undefined1 *)(param_1 + (long)param_3 + -1);
      puVar2 = (undefined1 *)(param_2 + (long)param_3 + -1);
      for (; param_3 != 0; param_3 = param_3 + -1) {
        *puVar1 = *puVar2;
        puVar1 = puVar1 + -1;
        puVar2 = puVar2 + -1;
      }
    }
  }
  return;
}


