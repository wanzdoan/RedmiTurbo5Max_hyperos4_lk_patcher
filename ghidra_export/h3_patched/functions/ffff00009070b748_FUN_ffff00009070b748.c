// ffff00009070b748  size=464  FUN_ffff00009070b748

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070b748(void)

{
  int iVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  undefined8 uVar5;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
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
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined4 local_40;
  undefined4 uStack_3c;
  undefined1 auStack_38 [4];
  int local_34;
  
  local_50 = 0;
  uStack_48 = 0;
  local_40 = 0x8000000;
  uStack_3c = 0;
  local_60 = 0;
  uStack_58 = 0;
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
  FUN_ffff00009077eb88(&local_b0,0,0x70);
  iVar1 = FUN_ffff000090772658(&local_b0,9,8,0x1f,8,0,s_1_3_1_1_motley_ffff0000907ebdb9,0x70);
  if (iVar1 == 0) {
    lVar2 = FUN_ffff00009077d24c(0x8000000);
    if (lVar2 != 0) {
      FUN_ffff00009077eb88(lVar2,0,0x8000000);
      lVar3 = FUN_ffff00009077d24c(0x8000);
      if (lVar3 == 0) {
        thunk_FUN_ffff0000907c2ad8(lVar2);
      }
      else {
        FUN_ffff00009077eb88(lVar3,0,0x8000);
        lVar4 = FUN_ffff00009077d24c(0x8000);
        if (lVar4 == 0) {
          thunk_FUN_ffff0000907c2ad8(lVar2);
          thunk_FUN_ffff0000907c2ad8(lVar3);
        }
        else {
          local_34 = 0;
          FUN_ffff00009077eb88(lVar4,0,0x8000);
          do {
            FUN_ffff00009070b584
                      (&local_34,auStack_38,lVar3,lVar4,&local_b0,lVar2,&uStack_3c,&local_40);
          } while (local_34 != 4);
          DAT_ffff000090816cd0 = ~DAT_ffff000090816cd0;
          FUN_ffff000090772910(&local_b0);
          local_f0 = 0;
          uStack_e8 = 0;
          local_e0 = 0;
          uStack_d8 = 0;
          local_d0 = 0;
          uStack_c8 = 0;
          local_c0 = 0;
          uStack_b8 = 0;
          FUN_ffff00009077f434(&local_f0,0x40,s_DATA_08x_ffff0000907f5aec,_DAT_ffff00009083d290);
          uVar5 = FUN_ffff00009077ef88(&local_f0);
          FUN_ffff00009070f9dc(&local_f0,uVar5);
          FUN_ffff00009070f9dc(lVar2,_DAT_ffff00009083d290);
          thunk_FUN_ffff0000907c2ad8(lVar4);
          thunk_FUN_ffff0000907c2ad8(lVar3);
          thunk_FUN_ffff0000907c2ad8(lVar2);
          FUN_ffff000090709d50(&DAT_ffff0000907df27e);
        }
      }
    }
  }
  else {
    FUN_ffff00009077de54(s_deflateInit2_failed_ffff0000907d5736);
  }
  return;
}


