
undefined8 FUN_ffff0000be09c1d8(ushort *param_1,int param_2)

{
  int iVar1;
  long lVar2;
  long lVar3;
  
  if (*(long *)(param_1 + 4) == 0) {
    lVar3 = FUN_ffff0000be05f250(1,4);
    if (lVar3 == 0) {
      return 0xfffffff0;
    }
    if (*(long *)(param_1 + 8) != 0) {
      FUN_ffff0000be0607b0(lVar3,*(long *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
      FUN_ffff0000be0a174c(*(undefined8 *)(param_1 + 8),*(long *)(param_1 + 4) << 2);
      thunk_FUN_ffff0000be09937c(*(undefined8 *)(param_1 + 8));
    }
    lVar2 = 4;
    param_1[4] = 1;
    param_1[5] = 0;
    param_1[6] = 0;
    param_1[7] = 0;
    *(long *)(param_1 + 8) = lVar3;
  }
  else {
    lVar3 = *(long *)(param_1 + 8);
    lVar2 = *(long *)(param_1 + 4) << 2;
  }
  FUN_ffff0000be060ae0(lVar3,0,lVar2);
  iVar1 = -param_2;
  if (-1 < param_2) {
    iVar1 = param_2;
  }
  **(int **)(param_1 + 8) = iVar1;
  *param_1 = (ushort)(param_2 >> 0x1f) | 1;
  return 0;
}


