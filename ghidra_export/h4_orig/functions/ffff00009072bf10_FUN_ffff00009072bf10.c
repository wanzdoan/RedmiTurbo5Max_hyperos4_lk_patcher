// ffff00009072bf10  size=320  FUN_ffff00009072bf10

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072bf10(undefined8 param_1,char param_2,long param_3)

{
  long lVar1;
  undefined4 uVar2;
  
  if (param_2 == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_meta_mode_md1_ccb_c_ffff0000907fd1d0,
               s_get_ccb_gear_val_ffff0000907e6075,0x1c2,0xe);
  }
  else {
    lVar1 = FUN_ffff000090741bdc(s_md1_ccb_cap_gear_ffff0000907d90ff);
    if ((lVar1 != 0) && (_DAT_ffff00009086d6f8 = FUN_ffff00009077d72c(), _DAT_ffff00009086d6f8 != 0)
       ) {
      switch(_DAT_ffff00009086d6f8) {
      case 1:
        uVar2 = 0x1600000;
        break;
      case 2:
        uVar2 = 0xc00000;
        break;
      case 3:
        *(undefined4 *)(param_3 + 8) = 0;
        return;
      case 4:
        uVar2 = 0x2000000;
        break;
      default:
        *(undefined4 *)(param_3 + 8) = 0;
        _DAT_ffff00009086d6f8 = 3;
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_un_support_gear_id__ffff0000907d5ee2,
                   s_ccci_get_ccb_size_ffff0000907e10e8,0x1f8);
        return;
      case 0xb:
        uVar2 = 0x400000;
        break;
      case 0xc:
        uVar2 = 0x4000000;
        break;
      case 0xd:
        uVar2 = 0x6000000;
        break;
      case 0xe:
        goto code_r0xffff00009072bfb0;
      case 0x10:
        uVar2 = 0xc000000;
      }
      goto LAB_ffff00009072bfb4;
    }
    _DAT_ffff00009086d6f8 = 0xe;
  }
code_r0xffff00009072bfb0:
  uVar2 = 0x1000000;
LAB_ffff00009072bfb4:
  *(undefined4 *)(param_3 + 8) = uVar2;
  return;
}


