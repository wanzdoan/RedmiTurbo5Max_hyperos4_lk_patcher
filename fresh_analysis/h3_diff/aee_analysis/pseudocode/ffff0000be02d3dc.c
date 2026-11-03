
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be02d3dc(long *param_1,int *param_2)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  undefined8 uVar6;
  long lVar7;
  ulong uVar8;
  
  uVar6 = 0xffffffff;
  if ((param_1 != (long *)0x0) && (param_2 != (int *)0x0)) {
    *param_2 = 0;
    *param_1 = 0;
    lVar7 = FUN_ffff0000be05f1bc(0xc000);
    *param_1 = lVar7;
    iVar3 = _DAT_ffff00000d044018;
    iVar5 = _DAT_ffff00000d044014;
    uVar2 = _DAT_ffff00000d04400c;
    uVar6 = 0;
    if (lVar7 != 0) {
      _DAT_ffff00000d040068 = 3;
      _DAT_ffff00000d01a070 = 1;
      _DAT_ffff00000d040070 = 1;
      _DAT_ffff00000d044fb0 = 0xc5acce55;
      DataSynchronizationBarrier(3,3,0);
      if (_DAT_ffff00000d044fb4 == 1) {
        if ((_DAT_ffff00000d04400c & 1) == 0) {
          uVar1 = _DAT_ffff00000d044018 - _DAT_ffff00000d044014 & _DAT_ffff00000d044004 * 4 - 1U;
        }
        else {
          uVar1 = _DAT_ffff00000d044004 << 2;
        }
        if (uVar1 == 0) {
          uVar6 = 0;
        }
        else {
          iVar4 = FUN_ffff0000be0611dc
                            (lVar7 + *param_2,0xc000 - *param_2,
                             s_____________________ETB_d__0x_08_ffff0000be0a48de,0,
                             0xffff00000d044000);
          *param_2 = *param_2 + iVar4;
          _DAT_ffff00000d044020 = 0;
          if (uVar1 < 4) {
            iVar4 = *param_2;
          }
          else {
            uVar8 = (ulong)(uVar1 >> 2);
            iVar4 = *param_2;
            do {
              iVar4 = FUN_ffff0000be0611dc
                                (lVar7 + iVar4,0xc000 - iVar4,s__08lx_ffff0000be0a5c25,
                                 _DAT_ffff00000d044010);
              uVar8 = uVar8 - 1;
              iVar4 = *param_2 + iVar4;
              *param_2 = iVar4;
            } while (uVar8 != 0);
          }
          if ((uVar1 & 3) != 0) {
            iVar4 = FUN_ffff0000be0611dc
                              (lVar7 + iVar4,0xc000 - iVar4,
                               s__warning__etb_depth_is_not_word__ffff0000be0bbeb4);
            iVar4 = *param_2 + iVar4;
            *param_2 = iVar4;
          }
          iVar5 = FUN_ffff0000be0611dc
                            (lVar7 + iVar4,0xc000 - iVar4,
                             s_depth___0x_lx_bytes__etb_status___ffff0000be0bbede,uVar1,uVar2,iVar5,
                             iVar3);
          uVar6 = 1;
          *param_2 = *param_2 + iVar5;
          _DAT_ffff00000d04401c = 0;
          _DAT_ffff00000d044014 = 0;
          _DAT_ffff00000d044018 = 0;
          DataSynchronizationBarrier(3,3,0);
          DataSynchronizationBarrier(3,3,0);
          _DAT_ffff00000d044fb0 = 0;
        }
      }
      else {
        FUN_ffff0000be05fd98(s__ETB_d__unlock_etb_failed_ffff0000be0bd237,0);
        uVar6 = 0;
      }
      if (*param_2 < 0xc001) {
        uVar6 = 1;
      }
      else {
        *param_2 = 0xc000;
      }
    }
  }
  return uVar6;
}


