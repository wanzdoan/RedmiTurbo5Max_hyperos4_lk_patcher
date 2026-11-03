
uint FUN_ffff0000b8028b74(long param_1,long param_2,uint param_3,uint param_4)

{
  uint uVar1;
  int iVar2;
  bool bVar3;
  uint uVar4;
  long lVar5;
  long lVar6;
  uint uVar7;
  undefined4 uVar8;
  uint local_dc;
  long local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  long local_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  local_b8 = 0;
  local_d0 = 0;
  local_c8 = 0;
  FUN_ffff0000b804d070(&local_d0,0,0x70);
  uVar4 = FUN_ffff0000b807a228(&local_d0,9,8,0x1f,8,0,s_1_3_1_1_motley_ffff0000b809546a,0x70);
  if (uVar4 == 0) {
    lVar5 = FUN_ffff0000b804bccc(0x10000);
    if (lVar5 != 0) {
      lVar6 = FUN_ffff0000b804bccc(0x10000);
      if (lVar6 == 0) {
        FUN_ffff0000b804c33c(s__TFA__obuf_malloc_fail_ffff0000b8097326);
        FUN_ffff0000b807a4e0(&local_d0);
        thunk_FUN_ffff0000b8082558(lVar5);
        return 0xfffffffb;
      }
      uVar7 = 0;
      local_dc = 0;
      do {
        uVar4 = 0x10000;
        local_c8 = CONCAT44(local_c8._4_4_,0x10000);
        local_b0 = CONCAT44(local_b0._4_4_,0x10000);
        if (param_3 >> 0x10 == 0) {
          uVar8 = 4;
          local_c8 = CONCAT44(local_c8._4_4_,param_3);
          uVar4 = param_3;
        }
        else {
          uVar8 = 0;
        }
        local_d0 = lVar5;
        local_b8 = lVar6;
        FUN_ffff0000b804cd40(lVar5,param_1 + (ulong)local_dc,uVar4);
        iVar2 = (int)local_c8;
        local_dc = (int)local_c8 + local_dc;
        local_d0 = lVar5;
        do {
          local_b0 = CONCAT44(local_b0._4_4_,0x10000);
          local_b8 = lVar6;
          uVar4 = FUN_ffff0000b807ab1c(&local_d0,uVar8);
          uVar1 = (0x10000 - (int)local_b0) + uVar7;
          if (uVar1 < param_4) {
            FUN_ffff0000b804cd40(param_2 + (ulong)uVar7,lVar6);
            uVar7 = uVar1;
          }
          else {
            FUN_ffff0000b804c33c(s__TFA__zip_buffer_overflow___ffff0000b809274a);
          }
          FUN_ffff0000b802c208();
        } while ((int)local_b0 == 0);
        bVar3 = 0xffff < param_3;
        param_3 = param_3 - iVar2;
      } while (bVar3);
      FUN_ffff0000b807a4e0(&local_d0);
      thunk_FUN_ffff0000b8082558(lVar5);
      thunk_FUN_ffff0000b8082558(lVar6);
      goto LAB_ffff0000b8028c20;
    }
    FUN_ffff0000b804c33c(s__TFA__ibuf_malloc_fail_ffff0000b80a04af);
    uVar4 = 0xfffffffb;
  }
  else {
    FUN_ffff0000b804c33c(s__TFA__ret____d_ffff0000b8091a08,uVar4);
  }
  FUN_ffff0000b807a4e0(&local_d0);
  uVar7 = 0;
LAB_ffff0000b8028c20:
  if (uVar4 != 1) {
    uVar7 = uVar4;
  }
  return uVar7;
}


