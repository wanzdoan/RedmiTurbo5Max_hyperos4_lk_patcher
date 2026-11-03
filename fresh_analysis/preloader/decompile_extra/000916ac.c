// File offset entry: 0x916ac

void FUN_020915ac(long param_1,long param_2,int param_3)

{
  long lVar1;
  
  for (lVar1 = 0; param_3 != (int)lVar1; lVar1 = lVar1 + 1) {
    *(undefined1 *)(param_1 + lVar1) = *(undefined1 *)(param_2 + lVar1);
  }
  return;
}


