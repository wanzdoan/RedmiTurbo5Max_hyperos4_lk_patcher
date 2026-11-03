// ffff00009070b584  size=452  FUN_ffff00009070b584

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff00009070b584
          (int *param_1,undefined4 *param_2,undefined8 param_3,undefined8 param_4,
          undefined8 *param_5,long param_6,int *param_7,uint *param_8)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  undefined4 uVar5;
  
  iVar2 = *param_7;
  _DAT_ffff00009083d28c = *param_8;
  _DAT_ffff00009083d288 = iVar2;
  *param_1 = (uint)(_DAT_ffff00009083d28c == 0) << 2;
  FUN_ffff00009077de54(s____Compress_expdb_offset__llx__s_ffff0000907dbe32,iVar2);
  do {
    uVar1 = _DAT_ffff00009083d28c;
    if (0x7fff < _DAT_ffff00009083d28c) {
      uVar1 = 0x8000;
    }
    FUN_ffff00009070b4bc(param_3,uVar1,_DAT_ffff00009083d288);
    uVar1 = _DAT_ffff00009083d28c;
    if (0x7fff < _DAT_ffff00009083d28c) {
      uVar1 = 0x8000;
    }
    uVar4 = FUN_ffff00009076c7c4(~DAT_ffff000090816cd0,param_3,uVar1);
    DAT_ffff000090816cd0 = ~uVar4;
    *(uint *)(param_5 + 1) = uVar1;
    _DAT_ffff00009083d28c = _DAT_ffff00009083d28c - uVar1;
    *param_5 = param_3;
    do {
      *(undefined4 *)(param_5 + 4) = 0x8000;
      iVar2 = *param_1;
      param_5[3] = param_4;
      uVar5 = FUN_ffff000090772f4c(param_5,iVar2);
      *param_2 = uVar5;
      iVar2 = *(int *)(param_5 + 4);
      iVar3 = 0x8000 - iVar2;
      if (0 < iVar3) {
        FUN_ffff00009077e858(param_6 + (ulong)_DAT_ffff00009083d284,param_4,iVar3);
        _DAT_ffff00009083d284 = _DAT_ffff00009083d284 + iVar3;
        iVar2 = *(int *)(param_5 + 4);
      }
      _DAT_ffff00009083d290 = (undefined4)param_5[5];
    } while (iVar2 == 0);
    _DAT_ffff00009083d288 = _DAT_ffff00009083d288 + uVar1;
  } while (_DAT_ffff00009083d28c != 0);
  FUN_ffff00009077de54(s____Compress_expdb_offset__llx__d_ffff0000907e5a35,_DAT_ffff00009083d288,0);
  *param_7 = _DAT_ffff00009083d288;
  *param_8 = _DAT_ffff00009083d28c;
  return 1;
}


