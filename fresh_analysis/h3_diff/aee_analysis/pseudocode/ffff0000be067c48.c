
undefined4 FUN_ffff0000be067c48(undefined8 param_1,long param_2,uint param_3)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  uint uVar5;
  long lVar6;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined1 local_68;
  
  local_68 = 0;
  local_78 = 0;
  uStack_70 = 0;
  if (0x300d < param_3) {
    uVar5 = 0;
    iVar3 = 0x3000;
    do {
      lVar6 = 0;
      do {
        iVar2 = FUN_ffff0000be060778
                          (param_2 + (ulong)uVar5 + lVar6,s_AND_ROMINFO_v_ffff0000be0b5bc7,0xd);
        if (iVar2 == 0) {
          lVar6 = param_2 + (ulong)uVar5 + lVar6;
          if (lVar6 != 0) {
            FUN_ffff0000be060ae0(&DAT_ffff0000be1323ac,0,0x24);
            FUN_ffff0000be0607b0(&DAT_ffff0000be1323ac,lVar6,0x24);
            lVar6 = FUN_ffff0000be067adc(param_1);
            if (lVar6 == 0) {
              return 0x10a009;
            }
            FUN_ffff0000be0607b0(&local_78,&DAT_ffff0000be1323c0,0x10);
            local_68 = 0;
            FUN_ffff0000be05fd98(s_Image_platform_name___s_ffff0000be0c2478,&local_78);
            FUN_ffff0000be0607b0(&local_78,lVar6,0x10);
            local_68 = 0;
            FUN_ffff0000be05fd98(s_Device_platform_name___s_ffff0000be0a6418,&local_78);
            iVar3 = FUN_ffff0000be060f54(lVar6,&DAT_ffff0000be1323c0,0x10);
            if (iVar3 == 0) {
              return 0;
            }
            return 0x10a00a;
          }
          uVar4 = 0;
          goto LAB_ffff0000be067da8;
        }
        lVar6 = lVar6 + 1;
      } while (lVar6 < 0x3000);
      uVar1 = iVar3 + 0x300d;
      uVar5 = uVar5 + 0x3000;
      iVar3 = iVar3 + 0x3000;
    } while (uVar1 < param_3);
  }
  uVar4 = 0x10a007;
LAB_ffff0000be067da8:
  FUN_ffff0000be05fd98(s_Find_no_rom_info_id___ret___0x_x_ffff0000be0b5bd5,uVar4);
  return 0x10a008;
}


