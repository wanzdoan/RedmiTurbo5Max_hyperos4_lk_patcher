
int FUN_ffff000090785294(long param_1,code *param_2,long *param_3,undefined4 *param_4)

{
  code *pcVar1;
  undefined4 uVar2;
  int iVar3;
  long lVar4;
  undefined8 local_28;
  
  pcVar1 = FUN_ffff00009078533c;
  if (param_2 != (code *)0x0) {
    pcVar1 = param_2;
  }
  local_28 = 0;
  uVar2 = FUN_ffff000090703444();
  iVar3 = (*pcVar1)(param_1,uVar2,&DAT_ffff000090832988,&local_28,(long)&local_28 + 4);
  if (-1 < iVar3) {
    lVar4 = FUN_ffff00009077d514(local_28 & 0xffffffff);
    if (lVar4 == 0) {
      iVar3 = -5;
    }
    else {
      *param_4 = (undefined4)local_28;
      FUN_ffff00009077eb20(lVar4,param_1 + (local_28 >> 0x20));
      *param_3 = lVar4;
    }
  }
  return iVar3;
}


