
/* RmcDcReqHandler::handleSetInitialAttachApnRequest(android::sp<RfxMclMessage> const&) */

void __thiscall RmcDcReqHandler::handleSetInitialAttachApnRequest(RmcDcReqHandler *this,sp *param_1)

{
  undefined8 uVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  int iVar4;
  long lVar5;
  undefined *puVar6;
  RfxAtResponse *pRVar7;
  uint uVar8;
  undefined4 uVar9;
  ulong uVar10;
  ulong uVar11;
  size_t sVar12;
  String8 *this_00;
  long **pplVar13;
  long *plVar14;
  long lVar15;
  char *__src;
  undefined8 *puVar16;
  long *local_168;
  long *local_160;
  long *local_158;
  long *local_150;
  long *local_148;
  long *local_140;
  long *local_138;
  char *local_130;
  void *pvStack_128;
  char *local_120;
  undefined8 uStack_118;
  char *local_110;
  undefined8 local_108;
  long *local_f8;
  RfxAtResponse *local_f0;
  RfxAtResponse *local_e8 [3];
  char local_d0 [96];
  uint local_70;
  long local_68;
  
  lVar5 = tpidr_el0;
  local_68 = *(long *)(lVar5 + 0x28);
  local_f8 = (long *)0x0;
  local_f0 = (RfxAtResponse *)0x0;
  puVar16 = *(undefined8 **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  uStack_118 = 0;
  local_120 = (char *)0x0;
  local_108 = 0;
  local_110 = (char *)0x0;
  pvStack_128 = (void *)0x0;
  local_130 = (char *)0x0;
  local_70 = 0;
  local_d0[8] = '\0';
  local_d0[9] = '\0';
  local_d0[10] = '\0';
  local_d0[0xb] = '\0';
  local_d0[0xc] = '\0';
  local_d0[0xd] = '\0';
  local_d0[0xe] = '\0';
  local_d0[0xf] = '\0';
  local_d0[0] = '\0';
  local_d0[1] = '\0';
  local_d0[2] = '\0';
  local_d0[3] = '\0';
  local_d0[4] = '\0';
  local_d0[5] = '\0';
  local_d0[6] = '\0';
  local_d0[7] = '\0';
  local_d0[0x18] = '\0';
  local_d0[0x19] = '\0';
  local_d0[0x1a] = '\0';
  local_d0[0x1b] = '\0';
  local_d0[0x1c] = '\0';
  local_d0[0x1d] = '\0';
  local_d0[0x1e] = '\0';
  local_d0[0x1f] = '\0';
  local_d0[0x10] = '\0';
  local_d0[0x11] = '\0';
  local_d0[0x12] = '\0';
  local_d0[0x13] = '\0';
  local_d0[0x14] = '\0';
  local_d0[0x15] = '\0';
  local_d0[0x16] = '\0';
  local_d0[0x17] = '\0';
  local_d0[0x28] = '\0';
  local_d0[0x29] = '\0';
  local_d0[0x2a] = '\0';
  local_d0[0x2b] = '\0';
  local_d0[0x2c] = '\0';
  local_d0[0x2d] = '\0';
  local_d0[0x2e] = '\0';
  local_d0[0x2f] = '\0';
  local_d0[0x20] = '\0';
  local_d0[0x21] = '\0';
  local_d0[0x22] = '\0';
  local_d0[0x23] = '\0';
  local_d0[0x24] = '\0';
  local_d0[0x25] = '\0';
  local_d0[0x26] = '\0';
  local_d0[0x27] = '\0';
  local_d0[0x38] = '\0';
  local_d0[0x39] = '\0';
  local_d0[0x3a] = '\0';
  local_d0[0x3b] = '\0';
  local_d0[0x3c] = '\0';
  local_d0[0x3d] = '\0';
  local_d0[0x3e] = '\0';
  local_d0[0x3f] = '\0';
  local_d0[0x30] = '\0';
  local_d0[0x31] = '\0';
  local_d0[0x32] = '\0';
  local_d0[0x33] = '\0';
  local_d0[0x34] = '\0';
  local_d0[0x35] = '\0';
  local_d0[0x36] = '\0';
  local_d0[0x37] = '\0';
  local_d0[0x48] = '\0';
  local_d0[0x49] = '\0';
  local_d0[0x4a] = '\0';
  local_d0[0x4b] = '\0';
  local_d0[0x4c] = '\0';
  local_d0[0x4d] = '\0';
  local_d0[0x4e] = '\0';
  local_d0[0x4f] = '\0';
  local_d0[0x40] = '\0';
  local_d0[0x41] = '\0';
  local_d0[0x42] = '\0';
  local_d0[0x43] = '\0';
  local_d0[0x44] = '\0';
  local_d0[0x45] = '\0';
  local_d0[0x46] = '\0';
  local_d0[0x47] = '\0';
  local_d0[0x58] = '\0';
  local_d0[0x59] = '\0';
  local_d0[0x5a] = '\0';
  local_d0[0x5b] = '\0';
  local_d0[0x5c] = '\0';
  local_d0[0x5d] = '\0';
  local_d0[0x5e] = '\0';
  local_d0[0x5f] = '\0';
  local_d0[0x50] = '\0';
  local_d0[0x51] = '\0';
  local_d0[0x52] = '\0';
  local_d0[0x53] = '\0';
  local_d0[0x54] = '\0';
  local_d0[0x55] = '\0';
  local_d0[0x56] = '\0';
  local_d0[0x57] = '\0';
  uVar10 = isValidInitialAttachApn(this,(char *)*puVar16);
  if ((uVar10 & 1) == 0) {
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) == 0) {
      mtkLogI("RmcDcReqHandler","[%d][%s] invalid apn %s, let modem select it",
              *(undefined4 *)(this + 8),"handleSetInitialAttachApnRequest",*puVar16);
    }
    else {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      mtkLogI(local_e8[0],"[%d][%s] invalid apn %s, let modem select it",*(undefined4 *)(this + 8),
              "handleSetInitialAttachApnRequest",*puVar16);
      android::String8::~String8((String8 *)local_e8);
    }
    if ((*(uint *)(this + 0x38) | 2) == 3) {
      android::String8::format("AT+EIAMDPREFER=1",&local_138);
      RfxBaseHandler::atSendCommand((String8 *)this,&local_138);
      pRVar7 = local_f0;
      if (local_f0 != (RfxAtResponse *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)(local_f0 + *(long *)(*(long *)local_f0 + -0x18)),&local_f0);
      }
      if (pRVar7 != local_f0) {
        android::sp_report_race();
      }
      local_f0 = local_e8[0];
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::~String8((String8 *)&local_138);
      if ((local_f0 == (RfxAtResponse *)0x0) ||
         (uVar10 = RfxAtResponse::isAtResponseFail(local_f0), (uVar10 & 1) != 0)) {
        uVar10 = __rfx_is_gt_mode();
        if ((uVar10 & 1) == 0) {
          mtkLogE("RmcDcReqHandler","[%d][%s] : AT+EIAMDPREFER=1 response error",
                  *(undefined4 *)(this + 8),"handleSetInitialAttachApnRequest");
        }
        else {
          local_e8[0] = (RfxAtResponse *)0x0;
          android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
          mtkLogE(local_e8[0],"[%d][%s] : AT+EIAMDPREFER=1 response error",*(undefined4 *)(this + 8)
                  ,"handleSetInitialAttachApnRequest");
          android::String8::~String8((String8 *)local_e8);
        }
        uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
        RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
        local_140 = *(long **)param_1;
        if (local_140 != (long *)0x0) {
          android::RefBase::incStrong((void *)((long)local_140 + *(long *)(*local_140 + -0x18)));
        }
        RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,2,local_e8,&local_140,0);
        plVar14 = local_f8;
        if (local_f8 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
        }
        if (plVar14 != local_f8) {
          android::sp_report_race();
        }
        local_f8 = local_138;
        local_138 = (long *)0x0;
        if (local_140 != (long *)0x0) {
          lVar15 = *local_140;
          pplVar13 = &local_140;
          plVar14 = local_140;
          goto LAB_004e9ef4;
        }
      }
      else {
        uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
        RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
        local_148 = *(long **)param_1;
        if (local_148 != (long *)0x0) {
          android::RefBase::incStrong((void *)((long)local_148 + *(long *)(*local_148 + -0x18)));
        }
        RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,0,local_e8,&local_148,0);
        plVar14 = local_f8;
        if (local_f8 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
        }
        if (plVar14 != local_f8) {
          android::sp_report_race();
        }
        local_f8 = local_138;
        local_138 = (long *)0x0;
        if (local_148 != (long *)0x0) {
          lVar15 = *local_148;
          pplVar13 = &local_148;
          plVar14 = local_148;
LAB_004e9ef4:
          local_138 = (long *)0x0;
          android::RefBase::decStrong
                    ((RefBase *)((long)plVar14 + *(long *)(lVar15 + -0x18)),pplVar13);
        }
      }
    }
    else {
      uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
      RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
      local_150 = *(long **)param_1;
      if (local_150 != (long *)0x0) {
        android::RefBase::incStrong((void *)((long)local_150 + *(long *)(*local_150 + -0x18)));
      }
      RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,0,local_e8,&local_150,0);
      plVar14 = local_f8;
      if (local_f8 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
      }
      if (plVar14 != local_f8) {
        android::sp_report_race();
      }
      local_f8 = local_138;
      local_138 = (long *)0x0;
      if (local_150 != (long *)0x0) {
        lVar15 = *local_150;
        pplVar13 = &local_150;
        plVar14 = local_150;
        goto LAB_004e9ef4;
      }
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_e8);
    goto LAB_004ea30c;
  }
  asprintf(&local_130,"%s",*puVar16);
  asprintf((char **)((ulong)&local_130 | 8),"%s",puVar16[4]);
  asprintf(&local_120,"%s",puVar16[5]);
  uStack_118 = CONCAT44(uStack_118._4_4_,*(undefined4 *)(puVar16 + 6));
  asprintf(&local_110,"%s",puVar16[1]);
  local_108 = CONCAT44(local_108._4_4_,*(undefined4 *)(puVar16 + 3));
  uVar8 = RmcDcCommonReqHandler::getIndexFromApnTable(*(uint *)(this + 8),(ApnTableReq *)&local_130)
  ;
  puVar6 = PTR_s_nLastReqNum_005d4288;
  uVar10 = (ulong)uVar8;
  if (((int)uVar8 < 0) ||
     (*(int *)(PTR_s_nLastReqNum_005d4288 + (ulong)*(uint *)(this + 8) * 4) <= (int)uVar8)) {
    uVar11 = __rfx_is_gt_mode();
    if ((uVar11 & 1) == 0) {
      mtkLogI("RmcDcReqHandler","[%d][%s] No APN found from table!! apnIndex=%d, apnCount=%d",
              (ulong)*(uint *)(this + 8),"handleSetInitialAttachApnRequest",uVar10,
              *(undefined4 *)(puVar6 + (ulong)*(uint *)(this + 8) * 4));
    }
    else {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      mtkLogI(local_e8[0],"[%d][%s] No APN found from table!! apnIndex=%d, apnCount=%d",
              (ulong)*(uint *)(this + 8),"handleSetInitialAttachApnRequest",uVar10,
              *(undefined4 *)(puVar6 + (ulong)*(uint *)(this + 8) * 4));
      android::String8::~String8((String8 *)local_e8);
    }
    uVar8 = 0;
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) == 0) goto LAB_004e98f4;
LAB_004e9afc:
    local_e8[0] = (RfxAtResponse *)0x0;
    android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
    mtkLogD(local_e8[0],"[%d][%s] cmdIndex = %d",*(undefined4 *)(this + 8),
            "handleSetInitialAttachApnRequest",uVar8);
    android::String8::~String8((String8 *)local_e8);
  }
  else {
    uVar8 = *(int *)(*(long *)(PTR_s_ApnCmdIndex_005d4290 + (ulong)*(uint *)(this + 8) * 8) +
                    uVar10 * 4) - 1;
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) != 0) goto LAB_004e9afc;
LAB_004e98f4:
    mtkLogD("RmcDcReqHandler","[%d][%s] cmdIndex = %d",*(undefined4 *)(this + 8),
            "handleSetInitialAttachApnRequest",uVar8);
  }
  __src = (char *)*puVar16;
  strncpy(local_d0,__src,99);
  sVar12 = strlen(__src);
  if (0x62 < sVar12) {
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) == 0) {
      uVar3 = *(undefined4 *)(this + 8);
      sVar12 = strlen((char *)*puVar16);
      mtkLogI("RmcDcReqHandler","[%d][%s] iaData->apn length: %lu",uVar3,
              "handleSetInitialAttachApnRequest",sVar12);
    }
    else {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      pRVar7 = local_e8[0];
      uVar3 = *(undefined4 *)(this + 8);
      sVar12 = strlen((char *)*puVar16);
      mtkLogI(pRVar7,"[%d][%s] iaData->apn length: %lu",uVar3,"handleSetInitialAttachApnRequest",
              sVar12);
      android::String8::~String8((String8 *)local_e8);
    }
    local_70 = local_70 & 0xffffff;
  }
  RmcDcCommonReqHandler::addEscapeSequence(local_d0);
  iVar4 = *(int *)(this + 0x38);
  uVar10 = __rfx_is_gt_mode();
  if (iVar4 < 2) {
    if (iVar4 == 0) {
LAB_004e9cd8:
      if ((uVar10 & 1) == 0) {
        mtkLogI("RmcDcReqHandler","[%d][%s] : send EIAAPN, apn = %s, id = %d",
                *(undefined4 *)(this + 8),"handleSetInitialAttachApnRequest",local_d0,uVar8);
      }
      else {
        local_e8[0] = (RfxAtResponse *)0x0;
        android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
        mtkLogI(local_e8[0],"[%d][%s] : send EIAAPN, apn = %s, id = %d",*(undefined4 *)(this + 8),
                "handleSetInitialAttachApnRequest",local_d0,uVar8);
        android::String8::~String8((String8 *)local_e8);
      }
      android::String8::format("AT+EIAAPN=\"%s\",%d",&local_138,local_d0,(ulong)uVar8);
      goto LAB_004ea010;
    }
    if (iVar4 == 1) goto LAB_004e9d30;
LAB_004e9f0c:
    if ((uVar10 & 1) != 0) {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      mtkLogE(local_e8[0],"iaMode = %d, should not be here",*(undefined4 *)(this + 0x38));
      this_00 = (String8 *)local_e8;
      goto LAB_004ea05c;
    }
    mtkLogE("RmcDcReqHandler","iaMode = %d, should not be here",*(undefined4 *)(this + 0x38));
    if (local_f0 == (RfxAtResponse *)0x0) goto LAB_004ea1c0;
LAB_004ea068:
    uVar10 = RfxAtResponse::isAtResponseFail(local_f0);
    if ((uVar10 & 1) == 0) {
      uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
      RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
      local_168 = *(long **)param_1;
      if (local_168 != (long *)0x0) {
        android::RefBase::incStrong((void *)((long)local_168 + *(long *)(*local_168 + -0x18)));
      }
      RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,0,local_e8,&local_168,0);
      plVar14 = local_f8;
      if (local_f8 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
      }
      if (plVar14 != local_f8) {
        android::sp_report_race();
      }
      local_f8 = local_138;
      local_138 = (long *)0x0;
      if (local_168 != (long *)0x0) {
        lVar15 = *local_168;
        pplVar13 = &local_168;
        plVar14 = local_168;
        goto LAB_004ea2b8;
      }
    }
    else {
      uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
      RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
      local_160 = *(long **)param_1;
      if (local_160 != (long *)0x0) {
        android::RefBase::incStrong((void *)((long)local_160 + *(long *)(*local_160 + -0x18)));
      }
      RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,2,local_e8,&local_160,0);
      plVar14 = local_f8;
      if (local_f8 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
      }
      if (plVar14 != local_f8) {
        android::sp_report_race();
      }
      local_f8 = local_138;
      local_138 = (long *)0x0;
      if (local_160 != (long *)0x0) {
        lVar15 = *local_160;
        pplVar13 = &local_160;
        plVar14 = local_160;
LAB_004ea2b8:
        local_138 = (long *)0x0;
        android::RefBase::decStrong((RefBase *)((long)plVar14 + *(long *)(lVar15 + -0x18)),pplVar13)
        ;
      }
    }
  }
  else {
    if (iVar4 != 2) {
      if (iVar4 != 3) goto LAB_004e9f0c;
      goto LAB_004e9cd8;
    }
LAB_004e9d30:
    if ((uVar10 & 1) == 0) {
      uVar1 = puVar16[1];
      uVar2 = puVar16[2];
      uVar3 = *(undefined4 *)(this + 8);
      uVar9 = RmcDcUtility::getAuthType(*(int *)(puVar16 + 3));
      mtkLogI("RmcDcReqHandler","[%d][%s] : send EIAAPN=\"%s\",%d,\"%s\",\"%s\",%d",uVar3,
              "handleSetInitialAttachApnRequest",local_d0,0,uVar1,uVar2,uVar9);
    }
    else {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      pRVar7 = local_e8[0];
      uVar1 = puVar16[1];
      uVar2 = puVar16[2];
      uVar3 = *(undefined4 *)(this + 8);
      uVar9 = RmcDcUtility::getAuthType(*(int *)(puVar16 + 3));
      mtkLogI(pRVar7,"[%d][%s] : send EIAAPN=\"%s\",%d,\"%s\",\"%s\",%d",uVar3,
              "handleSetInitialAttachApnRequest",local_d0,0,uVar1,uVar2,uVar9);
      android::String8::~String8((String8 *)local_e8);
    }
    uVar1 = puVar16[1];
    uVar2 = puVar16[2];
    uVar10 = RmcDcUtility::getAuthType(*(int *)(puVar16 + 3));
    android::String8::format
              ("AT+EIAAPN=\"%s\",%d,\"%s\",\"%s\",%d,\"%s\",\"%s\"",&local_138,local_d0,0,uVar1,
               uVar2,uVar10 & 0xffffffff,puVar16[4],puVar16[5]);
LAB_004ea010:
    RfxBaseHandler::atSendCommand((String8 *)this,&local_138);
    pRVar7 = local_f0;
    if (local_f0 != (RfxAtResponse *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)(local_f0 + *(long *)(*(long *)local_f0 + -0x18)),&local_f0);
    }
    if (pRVar7 != local_f0) {
      android::sp_report_race();
    }
    this_00 = (String8 *)&local_138;
    local_f0 = local_e8[0];
    local_e8[0] = (RfxAtResponse *)0x0;
LAB_004ea05c:
    android::String8::~String8(this_00);
    if (local_f0 != (RfxAtResponse *)0x0) goto LAB_004ea068;
LAB_004ea1c0:
    uVar10 = __rfx_is_gt_mode();
    if ((uVar10 & 1) == 0) {
      mtkLogE("RmcDcReqHandler","iaMode = %d, pAtResponse = null",*(undefined4 *)(this + 0x38));
    }
    else {
      local_e8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_e8,&DAT_001fbebf,"RmcDcReqHandler");
      mtkLogE(local_e8[0],"iaMode = %d, pAtResponse = null",*(undefined4 *)(this + 0x38));
      android::String8::~String8((String8 *)local_e8);
    }
    uVar3 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_e8);
    local_158 = *(long **)param_1;
    if (local_158 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_158 + *(long *)(*local_158 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_138,uVar3,2,local_e8,&local_158,0);
    plVar14 = local_f8;
    if (local_f8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
    }
    if (plVar14 != local_f8) {
      android::sp_report_race();
    }
    local_f8 = local_138;
    local_138 = (long *)0x0;
    if (local_158 != (long *)0x0) {
      lVar15 = *local_158;
      pplVar13 = &local_158;
      plVar14 = local_158;
      goto LAB_004ea2b8;
    }
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_e8);
  if (local_130 != (char *)0x0) {
    free(local_130);
    local_130 = (char *)0x0;
  }
  if (pvStack_128 != (void *)0x0) {
    free(pvStack_128);
    pvStack_128 = (void *)0x0;
  }
  if (local_120 != (char *)0x0) {
    free(local_120);
    local_120 = (char *)0x0;
  }
  if (local_110 != (char *)0x0) {
    free(local_110);
    local_110 = (char *)0x0;
  }
LAB_004ea30c:
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_f8);
  if (local_f8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_f8 + *(long *)(*local_f8 + -0x18)),&local_f8);
  }
  if (local_f0 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_f0 + *(long *)(*(long *)local_f0 + -0x18)),&local_f0);
  }
  if (*(long *)(lVar5 + 0x28) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


