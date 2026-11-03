// ffff000090729f38  size=232  FUN_ffff000090729f38

undefined8 FUN_ffff000090729f38(undefined1 *param_1,ulong param_2,long param_3,ulong *param_4)

{
  ulong uVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  ulong uVar5;
  undefined2 local_54;
  undefined1 local_52;
  
  uVar3 = 0xfffffff8;
  if ((((param_1 != (undefined1 *)0x0) && (param_2 < 0x1f8)) && (param_3 != 0)) &&
     (param_4 != (ulong *)0x0)) {
    uVar1 = param_2 << 1 | 1;
    if (*param_4 < uVar1) {
      uVar3 = 0xfffffff7;
    }
    else {
      if (param_2 != 0) {
        lVar4 = param_3 + 1;
        uVar5 = param_2;
        do {
          local_52 = 0;
          local_54 = 0;
          iVar2 = FUN_ffff00009077f434(&local_54,3,&DAT_ffff0000907f5b36,*param_1);
          if (iVar2 < 2) {
            return 0xfffffff9;
          }
          uVar5 = uVar5 - 1;
          *(undefined2 *)(lVar4 + -1) = local_54;
          lVar4 = lVar4 + 2;
          param_1 = param_1 + 1;
        } while (uVar5 != 0);
      }
      uVar3 = 0;
      *(undefined1 *)(param_3 + param_2 * 2) = 0;
      *param_4 = uVar1;
    }
  }
  return uVar3;
}


