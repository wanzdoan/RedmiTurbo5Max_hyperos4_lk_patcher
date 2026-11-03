// ffff0000907c988c  size=164  FUN_ffff0000907c988c

int FUN_ffff0000907c988c(undefined8 param_1,ulong param_2,code *param_3,undefined8 param_4)

{
  int iVar1;
  undefined1 auStack_440 [1024];
  
  if (param_2 < 0x401) {
    FUN_ffff00009077eb88(auStack_440,0,0x400);
    iVar1 = (*param_3)(param_4,auStack_440,param_2);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907c70d0(param_1,auStack_440,param_2);
    }
    FUN_ffff0000907cc428(auStack_440,0x400);
  }
  else {
    iVar1 = -4;
  }
  return iVar1;
}


