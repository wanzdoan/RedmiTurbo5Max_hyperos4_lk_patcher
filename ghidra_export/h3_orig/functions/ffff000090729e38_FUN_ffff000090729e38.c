// ffff000090729e38  size=256  FUN_ffff000090729e38

undefined8 FUN_ffff000090729e38(long param_1,ulong param_2,undefined1 *param_3,ulong *param_4)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  int *piVar4;
  ulong uVar5;
  ulong uVar6;
  undefined1 local_34;
  undefined1 local_33;
  undefined1 local_32;
  
  uVar2 = 0xfffffff8;
  if ((((param_1 != 0) && (param_2 < 0x3f0)) && ((param_2 & 1) == 0)) &&
     ((param_3 != (undefined1 *)0x0 && (param_4 != (ulong *)0x0)))) {
    uVar5 = param_2 + 1 >> 1;
    if (*param_4 < uVar5) {
      uVar2 = 0xfffffff7;
    }
    else {
      if (param_2 != 0) {
        uVar6 = 0;
        do {
          local_32 = 0;
          local_34 = *(undefined1 *)(param_1 + uVar6);
          local_33 = ((undefined1 *)(param_1 + uVar6))[1];
          iVar1 = FUN_ffff00009077d898();
          if (((iVar1 == 0) || (iVar1 = FUN_ffff00009077d898(local_33), iVar1 == 0)) ||
             ((uVar3 = FUN_ffff00009077d930(&local_34,0,0x10), 0xff < uVar3 ||
              (piVar4 = (int *)FUN_ffff00009077d8f0(), *piVar4 == 0x22)))) {
            return 0xfffffff9;
          }
          uVar6 = uVar6 + 2;
          *param_3 = (char)uVar3;
          param_3 = param_3 + 1;
        } while (uVar6 < param_2);
      }
      uVar2 = 0;
      *param_4 = uVar5;
    }
  }
  return uVar2;
}


