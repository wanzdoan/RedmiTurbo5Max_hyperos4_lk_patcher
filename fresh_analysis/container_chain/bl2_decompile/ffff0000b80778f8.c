
undefined8 FUN_ffff0000b80778f8(long *param_1,long param_2)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  
  if (param_2 == 0) {
LAB_ffff0000b8077978:
    uVar3 = 0xffffffff;
  }
  else {
    iVar1 = (int)param_1[1];
    iVar2 = *(int *)((long)param_1 + 0xc);
    if (iVar2 < iVar1) {
      lVar4 = *param_1;
    }
    else {
      lVar4 = thunk_FUN_ffff0000b804bccc((long)iVar1 << 4);
      if (lVar4 == 0) goto LAB_ffff0000b8077978;
      thunk_FUN_ffff0000b804cd40(lVar4,*param_1,(long)(int)param_1[1] << 3);
      thunk_FUN_ffff0000b8082558(*param_1);
      *param_1 = lVar4;
      *(int *)(param_1 + 1) = iVar1 << 1;
    }
    uVar3 = 0;
    *(long *)(lVar4 + (long)iVar2 * 8) = param_2;
    *(int *)((long)param_1 + 0xc) = iVar2 + 1;
  }
  return uVar3;
}


