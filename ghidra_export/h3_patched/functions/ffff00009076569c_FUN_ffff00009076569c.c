// ffff00009076569c  size=576  FUN_ffff00009076569c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff00009076569c
          (int *param_1,undefined4 *param_2,undefined8 param_3,undefined8 param_4,
          undefined8 *param_5,long param_6,int *param_7,uint *param_8)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  undefined4 uVar5;
  long lVar6;
  
  iVar3 = *param_7;
  _DAT_ffff00009087cc38 = *param_8;
  _DAT_ffff00009087cc34 = iVar3;
  *param_1 = (uint)(_DAT_ffff00009087cc38 == 0) << 2;
  FUN_ffff00009077de54(s____Compress_expdb_offset__llx__s_ffff0000907dbe1b,iVar3);
  do {
    uVar1 = _DAT_ffff00009087cc38;
    lVar6 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
    if (uVar1 < 0x8001) {
      if (lVar6 != 0) {
        uVar4 = FUN_ffff00009077bdf4(lVar6,param_3,_DAT_ffff00009087cc34,uVar1);
        if (uVar1 != uVar4) goto LAB_ffff000090765780;
        goto LAB_ffff00009076578c;
      }
LAB_ffff0000907657c0:
      FUN_ffff00009077de54
                (s_kedump__Partition___s__is_not_ex_ffff0000907d3e02,s_expdb_ffff0000907d5730);
    }
    else {
      if (lVar6 == 0) goto LAB_ffff0000907657c0;
      iVar3 = FUN_ffff00009077bdf4(lVar6,param_3,_DAT_ffff00009087cc34,0x8000);
      if (iVar3 != 0x8000) {
LAB_ffff000090765780:
        FUN_ffff00009077de54(s_read_expdb_fail_ffff0000907e00e9);
      }
LAB_ffff00009076578c:
      FUN_ffff00009077bce4(lVar6);
    }
    uVar1 = _DAT_ffff00009087cc38;
    if (0x7fff < _DAT_ffff00009087cc38) {
      uVar1 = 0x8000;
    }
    uVar4 = FUN_ffff00009076c7c4(~DAT_ffff000090821930,param_3,uVar1);
    DAT_ffff000090821930 = ~uVar4;
    *(uint *)(param_5 + 1) = uVar1;
    *param_5 = param_3;
    _DAT_ffff00009087cc38 = _DAT_ffff00009087cc38 - uVar1;
    do {
      *(undefined4 *)(param_5 + 4) = 0x8000;
      iVar3 = *param_1;
      param_5[3] = param_4;
      uVar5 = FUN_ffff000090772f4c(param_5,iVar3);
      *param_2 = uVar5;
      iVar3 = *(int *)(param_5 + 4);
      iVar2 = 0x8000 - iVar3;
      if (0 < iVar2) {
        FUN_ffff00009077e858(param_6 + (ulong)_DAT_ffff00009087cc30,param_4,iVar2);
        _DAT_ffff00009087cc30 = _DAT_ffff00009087cc30 + iVar2;
        iVar3 = *(int *)(param_5 + 4);
      }
      _DAT_ffff00009087cc3c = (undefined4)param_5[5];
    } while (iVar3 == 0);
    _DAT_ffff00009087cc34 = _DAT_ffff00009087cc34 + uVar1;
    if (_DAT_ffff00009087cc38 == 0) {
      FUN_ffff00009077de54
                (s____Compress_expdb_offset__llx__d_ffff0000907e5a1e,_DAT_ffff00009087cc34,0);
      *param_7 = _DAT_ffff00009087cc34;
      *param_8 = _DAT_ffff00009087cc38;
      return 1;
    }
  } while( true );
}


