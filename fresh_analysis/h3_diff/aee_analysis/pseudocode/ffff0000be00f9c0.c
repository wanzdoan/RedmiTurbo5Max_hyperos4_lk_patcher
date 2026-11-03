
void FUN_ffff0000be00f9c0(long param_1,ulong param_2,long param_3,long param_4)

{
  ulong uVar1;
  bool bVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  ulong local_68;
  
  if (param_2 == 0) {
    param_2 = FUN_ffff0000be010288(param_1);
  }
  lVar5 = 0;
  uVar6 = 0;
  param_3 = param_3 + param_1;
  lVar4 = 0;
  do {
    local_68 = param_3 - param_1;
    if ((int)(local_68 >> 0xc) < 2) {
      bVar2 = true;
      param_1 = lVar4;
    }
    else {
      uVar1 = 0x1000;
      do {
        uVar3 = uVar1;
        lVar4 = param_1 + uVar3;
        uVar6 = FUN_ffff0000be010288(lVar4);
        if (uVar6 == 0) {
LAB_ffff0000be00fae8:
          FUN_ffff0000be063b40
                    (s_kedump__expected_pa_0x_llx__va_0_ffff0000be0c038d,param_2 + uVar3,lVar4);
          FUN_ffff0000be05fd98
                    (s_kedump__expected_pa_0x_llx__va_0_ffff0000be0c038d,param_2 + uVar3,lVar4);
        }
        else {
          if ((DAT_ffff0000be0fd038 == 0) || (DAT_ffff0000be0fd040 == 0)) {
            FUN_ffff0000be063b40(s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5);
            FUN_ffff0000be05fd98
                      (s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5,DAT_ffff0000be0fd038,
                       DAT_ffff0000be0fd040);
LAB_ffff0000be00fac8:
            FUN_ffff0000be063b40(s_kedump__non_dram_address_0x_llx_ffff0000be0c5fa2,uVar6);
            FUN_ffff0000be05fd98(s_kedump__non_dram_address_0x_llx_ffff0000be0c5fa2,uVar6);
            goto LAB_ffff0000be00fae8;
          }
          if ((uVar6 < DAT_ffff0000be0fd038) || (DAT_ffff0000be0fd040 <= uVar6))
          goto LAB_ffff0000be00fac8;
          uVar1 = param_2 + uVar3;
          if (uVar1 != uVar6) {
            FUN_ffff0000be063b40
                      (s_kedump__non_cont_0x_llx_found_va_ffff0000be0c03b5,uVar1,param_1 + uVar3,
                       uVar6);
            FUN_ffff0000be05fd98
                      (s_kedump__non_cont_0x_llx_found_va_ffff0000be0c03b5,uVar1,param_1 + uVar3,
                       uVar6);
            bVar2 = false;
            local_68 = uVar3;
            goto LAB_ffff0000be00fba8;
          }
        }
        uVar1 = uVar3 + 0x1000;
      } while ((local_68 & 0xffffffff000) != uVar3 + 0x1000);
      bVar2 = true;
LAB_ffff0000be00fba8:
      param_1 = param_1 + uVar3;
    }
    FUN_ffff0000be0634dc();
    FUN_ffff0000be063600(lVar5 + param_4);
    uVar1 = 0;
    if (param_2 <= DAT_ffff0000be0fd040) {
      uVar1 = param_2;
    }
    if (DAT_ffff0000be0fd040 != 0) {
      param_2 = uVar1;
    }
    FUN_ffff0000be0636d8(param_2,local_68);
    lVar5 = local_68 + lVar5;
    param_2 = uVar6;
    lVar4 = param_1;
    if (bVar2) {
      return;
    }
  } while( true );
}


