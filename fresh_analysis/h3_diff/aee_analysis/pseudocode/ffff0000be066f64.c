
int FUN_ffff0000be066f64(long param_1,uint param_2,long param_3,int param_4)

{
  long lVar1;
  long lVar2;
  long lVar3;
  byte bVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 local_160;
  undefined8 local_158;
  byte local_150 [32];
  undefined8 local_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined7 uStack_60;
  undefined1 local_59;
  undefined6 uStack_58;
  byte bStack_52;
  undefined4 local_34;
  
  iVar5 = 0x10b008;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  uStack_58 = 0;
  bStack_52 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_59 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_170 = DAT_ffff0000be0c76f0;
  uStack_168 = DAT_ffff0000be0c76f8;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_150[0x18] = 0;
  local_150[0x19] = 0;
  local_150[0x1a] = 0;
  local_150[0x1b] = 0;
  local_150[0x1c] = 0;
  local_150[0x1d] = 0;
  local_150[0x1e] = 0;
  local_150[0x1f] = 0;
  local_130 = 0;
  local_34 = 0;
  local_150[8] = 0;
  local_150[9] = 0;
  local_150[10] = 0;
  local_150[0xb] = 0;
  local_150[0xc] = 0;
  local_150[0xd] = 0;
  local_150[0xe] = 0;
  local_150[0xf] = 0;
  local_150[0x10] = 0;
  local_150[0x11] = 0;
  local_150[0x12] = 0;
  local_150[0x13] = 0;
  local_150[0x14] = 0;
  local_150[0x15] = 0;
  local_150[0x16] = 0;
  local_150[0x17] = 0;
  local_158 = DAT_ffff0000be0c7708;
  local_150[0] = 0;
  local_150[1] = 0;
  local_150[2] = 0;
  local_150[3] = 0;
  local_150[4] = 0;
  local_150[5] = 0;
  local_150[6] = 0;
  local_150[7] = 0;
  local_160 = DAT_ffff0000be0c7700;
  if (param_1 != 0) {
    if (param_2 == 0) {
      iVar5 = 0x10b009;
    }
    else if (param_3 == 0) {
      iVar5 = 0x10b00a;
    }
    else if (param_4 == 0x100) {
      if (param_2 < 0xbf) {
        FUN_ffff0000be060ae0(param_3,0,0x100);
        local_34 = 0;
        iVar5 = FUN_ffff0000be029a00(&local_34);
        if (iVar5 == 0) {
          lVar1 = param_3 + 1;
          FUN_ffff0000be0607b0(lVar1,&local_34,4);
          local_34 = 0;
          iVar5 = FUN_ffff0000be029a00(&local_34);
          if (iVar5 == 0) {
            FUN_ffff0000be0607b0(param_3 + 5,&local_34,4);
            local_34 = 0;
            iVar5 = FUN_ffff0000be029a00(&local_34);
            if (iVar5 == 0) {
              FUN_ffff0000be0607b0(param_3 + 9,&local_34,4);
              local_34 = 0;
              iVar5 = FUN_ffff0000be029a00(&local_34);
              if (iVar5 == 0) {
                FUN_ffff0000be0607b0(param_3 + 0xd,&local_34,4);
                local_34 = 0;
                iVar5 = FUN_ffff0000be029a00(&local_34);
                if (iVar5 == 0) {
                  FUN_ffff0000be0607b0(param_3 + 0x11,&local_34,4);
                  local_34 = 0;
                  iVar5 = FUN_ffff0000be029a00(&local_34);
                  if (iVar5 == 0) {
                    FUN_ffff0000be0607b0(param_3 + 0x15,&local_34,4);
                    local_34 = 0;
                    iVar5 = FUN_ffff0000be029a00(&local_34);
                    if (iVar5 == 0) {
                      FUN_ffff0000be0607b0(param_3 + 0x19,&local_34,4);
                      local_34 = 0;
                      iVar5 = FUN_ffff0000be029a00(&local_34);
                      if (iVar5 == 0) {
                        lVar7 = param_3 + 0x21;
                        FUN_ffff0000be0607b0(param_3 + 0x1d,&local_34,4);
                        FUN_ffff0000be0607b0(lVar7,&local_170,0x20);
                        *(undefined1 *)((lVar7 - (ulong)param_2) + 0xde) = 1;
                        FUN_ffff0000be0607b0((param_3 - (ulong)param_2) + 0x100,param_1);
                        iVar5 = FUN_ffff0000be067284(lVar1,0x20,&local_130,0xdf);
                        if (iVar5 == 0) {
                          lVar6 = 0;
                          do {
                            lVar3 = param_3 + lVar6;
                            lVar2 = lVar6 + 2;
                            bVar4 = *(byte *)((long)&local_130 + lVar6 + 1);
                            *(byte *)(lVar3 + 0x21) =
                                 *(byte *)(lVar3 + 0x21) ^ *(byte *)((long)&local_130 + lVar6);
                            *(byte *)(lVar3 + 0x22) = *(byte *)(lVar3 + 0x22) ^ bVar4;
                            lVar6 = lVar2;
                          } while (lVar2 != 0xde);
                          *(byte *)(param_3 + 0xff) = *(byte *)(param_3 + 0xff) ^ bStack_52;
                          iVar5 = FUN_ffff0000be067284(lVar7,0xdf,local_150,0x20);
                          if (iVar5 == 0) {
                            lVar7 = 0;
                            do {
                              *(byte *)(lVar1 + lVar7) = *(byte *)(lVar1 + lVar7) ^ local_150[lVar7]
                              ;
                              lVar7 = lVar7 + 1;
                            } while (lVar7 != 0x20);
                            return 0;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      else {
        iVar5 = 0x10b00c;
      }
    }
    else {
      iVar5 = 0x10b00b;
    }
  }
  FUN_ffff0000be05fd98(s__oaep__encode_fail__0x_x__ffff0000be0c6993,iVar5);
  return iVar5;
}


