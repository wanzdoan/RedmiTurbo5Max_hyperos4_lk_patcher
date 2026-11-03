// ffff000090765934  size=576  FUN_ffff000090765934

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff000090765934
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
  _DAT_ffff00009087ccd0 = *param_8;
  _DAT_ffff00009087cccc = iVar3;
  *param_1 = (uint)(_DAT_ffff00009087ccd0 == 0) << 2;
  FUN_ffff00009077e11c(s____Compress_expdb_offset__llx__s_ffff0000907dbf3d,iVar3);
  do {
    uVar1 = _DAT_ffff00009087ccd0;
    lVar6 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
    if (uVar1 < 0x8001) {
      if (lVar6 != 0) {
        uVar4 = FUN_ffff00009077c0bc(lVar6,param_3,_DAT_ffff00009087cccc,uVar1);
        if (uVar1 != uVar4) goto LAB_ffff000090765a18;
        goto LAB_ffff000090765a24;
      }
LAB_ffff000090765a58:
      FUN_ffff00009077e11c
                (s_kedump__Partition___s__is_not_ex_ffff0000907d3e3f,s_expdb_ffff0000907d5780);
    }
    else {
      if (lVar6 == 0) goto LAB_ffff000090765a58;
      iVar3 = FUN_ffff00009077c0bc(lVar6,param_3,_DAT_ffff00009087cccc,0x8000);
      if (iVar3 != 0x8000) {
LAB_ffff000090765a18:
        FUN_ffff00009077e11c(s_read_expdb_fail_ffff0000907e01a3);
      }
LAB_ffff000090765a24:
      FUN_ffff00009077bfac(lVar6);
    }
    uVar1 = _DAT_ffff00009087ccd0;
    if (0x7fff < _DAT_ffff00009087ccd0) {
      uVar1 = 0x8000;
    }
    uVar4 = FUN_ffff00009076ca8c(~DAT_ffff000090821950,param_3,uVar1);
    DAT_ffff000090821950 = ~uVar4;
    *(uint *)(param_5 + 1) = uVar1;
    *param_5 = param_3;
    _DAT_ffff00009087ccd0 = _DAT_ffff00009087ccd0 - uVar1;
    do {
      *(undefined4 *)(param_5 + 4) = 0x8000;
      iVar3 = *param_1;
      param_5[3] = param_4;
      uVar5 = FUN_ffff000090773214(param_5,iVar3);
      *param_2 = uVar5;
      iVar3 = *(int *)(param_5 + 4);
      iVar2 = 0x8000 - iVar3;
      if (0 < iVar2) {
        FUN_ffff00009077eb20(param_6 + (ulong)_DAT_ffff00009087ccc8,param_4,iVar2);
        _DAT_ffff00009087ccc8 = _DAT_ffff00009087ccc8 + iVar2;
        iVar3 = *(int *)(param_5 + 4);
      }
      _DAT_ffff00009087ccd4 = (undefined4)param_5[5];
    } while (iVar3 == 0);
    _DAT_ffff00009087cccc = _DAT_ffff00009087cccc + uVar1;
    if (_DAT_ffff00009087ccd0 == 0) {
      FUN_ffff00009077e11c
                (s____Compress_expdb_offset__llx__d_ffff0000907e5b49,_DAT_ffff00009087cccc,0);
      *param_7 = _DAT_ffff00009087cccc;
      *param_8 = _DAT_ffff00009087ccd0;
      return 1;
    }
  } while( true );
}


